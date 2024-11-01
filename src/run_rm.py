import csv
import os
import subprocess
from dotenv import load_dotenv


# Function to clone a repository
def clone_repository(repo_name, repo_url):
    repo_path = os.path.join(clone_dir, repo_name.split('/')[-1])
    try:
        # Check if the repository has already been cloned
        if not os.path.exists(repo_path):
            print(f'Cloning {repo_name} into {repo_path}')
            subprocess.run(['git', 'clone', repo_url, repo_path], check=True)
        else:
            print(f'Repository {repo_name} already cloned.')

        return repo_path
    except subprocess.CalledProcessError as e:
        error_message = f"Error cloning repository {repo_name}: {str(e)}"
        print(error_message)
        write_log(repo_name, error_message)
        return None


# Function to run RefactoringMiner
def run_refactoring_miner(repo_path, repo_name):
    try:
        json_output = os.path.join(clone_dir, f'{repo_name.split("/")[-1]}.json')
        print(f'Running RefactoringMiner for {repo_name}')

        # Command to execute RefactoringMiner
        # command = [
        #     java_path,
        #     '-cp', refactoring_miner_classpath,
        #     refactoring_miner_class,
        #     '-a', repo_path,
        #     default_branch,
        #     '-json', json_output
        # ]
        command = [
            refactoring_miner_path,
            '-a', repo_path,
            '-json', json_output
        ]

        subprocess.run(command, check=True)
    except subprocess.CalledProcessError as e:
        error_message = f"Error running RefactoringMiner for {repo_name}: {str(e)}"
        print(error_message)
        write_log(repo_name, error_message)


# Function to write logs to a file
def write_log(repo_name, message):
    log_file = os.path.join(clone_dir, f'{repo_name.split("/")[-1]}.log')
    with open(log_file, 'a', encoding='utf-8') as f:
        f.write(message + '\n')


def process():
    # Read the CSV file and clone repositories
    with open(csv_path, newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)

        for row in reader:
            repo_name = row['name']
            repo_url = f"https://github.com/{repo_name}.git"

            # Clone the repository and get the clone path
            repo_path = clone_repository(repo_name, repo_url)

            if repo_path is None:
                continue
            try:
                # Run RefactoringMiner on the cloned repository
                run_refactoring_miner(repo_path, repo_name)
            except subprocess.CalledProcessError as e:
                error_message = f"Error running RefactoringMiner for {repo_name}: {str(e)}"
                print(error_message)
                write_log(repo_name, error_message)
                continue  # Skip to the next repository

            write_log(repo_name, f"Processing completed for {repo_name}")

    print("All repositories have been processed.")


if __name__ == '__main__':
    # Load environment variables from .env file
    load_dotenv()

    # Environment variables
    csv_path = os.getenv('CSV_PATH', 'repositories.csv')
    clone_dir = os.getenv('CLONE_DIR', 'repositories')
    refactoring_miner_path = os.getenv('REFACTORING_MINER_PATH',
                                       './RefactoringMiner-3.0.7/bin/RefactoringMiner')

    # Create the repositories directory if it does not exist
    if not os.path.exists(clone_dir):
        os.makedirs(clone_dir)

    process()
