import csv
import os
import subprocess
import requests
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()

# Environment variables
csv_path = os.getenv('CSV_PATH', 'repositories.csv')
clone_dir = os.getenv('CLONE_DIR', 'repositories')
java_path = os.getenv('JAVA_PATH', '/usr/lib/jvm/java-17-oracle/bin/java')
refactoring_miner_classpath = os.getenv('REFACTORING_MINER_CLASSPATH', './RefactoringMiner-3.0.7/bin:./RefactoringMiner-3.0.7/lib/*')
refactoring_miner_class = os.getenv('REFACTORING_MINER_CLASS', 'org.refactoringminer.RefactoringMiner')
github_token = os.getenv('GITHUB_OAUTH_TOKEN')

# Create the repositories directory if it does not exist
if not os.path.exists(clone_dir):
    os.makedirs(clone_dir)

# Function to retrieve the default branch of a repository
def get_default_branch(repo_name):
    headers = {'Authorization': f'token {github_token}'}
    url = f"https://api.github.com/repos/{repo_name}"
    
    response = requests.get(url, headers=headers)
    if response.status_code == 200:
        data = response.json()
        return data.get('default_branch')
    else:
        print(f"Error accessing repository: {response.status_code}")
        return None

# Function to clone a repository
def clone_repository(repo_name, repo_url):
    repo_path = os.path.join(clone_dir, repo_name.split('/')[-1])
    
    # Check if the repository has already been cloned
    if not os.path.exists(repo_path):
        print(f'Cloning {repo_name} into {repo_path}')
        subprocess.run(['git', 'clone', repo_url, repo_path], check=True)
    else:
        print(f'Repository {repo_name} already cloned.')
    
    return repo_path

# Function to run RefactoringMiner
def run_refactoring_miner(repo_path, repo_name, default_branch):
    json_output = os.path.join(clone_dir, f'{repo_name.split("/")[-1]}.json')
    print(f'Running RefactoringMiner for {repo_name} on branch {default_branch}')
    
    # Command to execute RefactoringMiner
    command = [
        java_path,
        '-cp', refactoring_miner_classpath,
        refactoring_miner_class,
        '-a', repo_path,
        default_branch,
        '-json', json_output
    ]
    
    subprocess.run(command, check=True)

# Function to write logs to a file
def write_log(repo_name, message):
    log_file = os.path.join(clone_dir, f'{repo_name.split("/")[-1]}.log')
    with open(log_file, 'a', encoding='utf-8') as f:
        f.write(message + '\n')

# Read the CSV file and clone repositories
with open(csv_path, newline='', encoding='utf-8') as csvfile:
    reader = csv.DictReader(csvfile)
    
    for row in reader:
        repo_name = row['name']
        repo_url = f"https://github.com/{repo_name}.git"
        default_branch = get_default_branch(repo_name)

        # Initial log for each repository
        write_log(repo_name, f"Processing repository {repo_name}, default branch: {default_branch}")
        print(default_branch)
        
        try:
            # Clone the repository and get the clone path
            repo_path = clone_repository(repo_name, repo_url)
        except subprocess.CalledProcessError as e:
            error_message = f"Error cloning repository {repo_name}: {str(e)}"
            print(error_message)
            write_log(repo_name, error_message)
            continue  # Skip to the next repository
        
        try:
            # Run RefactoringMiner on the cloned repository
            run_refactoring_miner(repo_path, repo_name, default_branch)
        except subprocess.CalledProcessError as e:
            error_message = f"Error running RefactoringMiner for {repo_name}: {str(e)}"
            print(error_message)
            write_log(repo_name, error_message)
            continue  # Skip to the next repository

        write_log(repo_name, f"Processing completed for {repo_name}")

print("All repositories have been processed.")
