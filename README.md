## Requirements

1. **Software Dependencies**:
   - **Python 3.10+** with packages in `requirements.txt`
   - **Git**: Required to clone repositories.
   - **Java 17**: RefactoringMiner requires Java 17 to perform the analysis.

2. **Environment Variables**:
   - Create a `.env` file based on `.env.example` and set the variables:
     - `CSV_PATH`: Path to the CSV file containing the list of repositories to be processed.
     - `CLONE_DIR`: Directory where repositories will be cloned.
     - `JAVA_PATH`: Path to the Java executable.
    - `REFACTORING_MINER_PATH`: Path to RefactoringMiner

## Running the Script

1. **Environment Setup**:
   - Ensure all dependencies are installed. You can install the required Python packages with:
     ```bash
     pip install -r requirements.txt
     ```

2. **Configuring the Repositories CSV**:
   - The CSV file specified in `CSV_PATH` should contain a column named `name` with the GitHub repository names (format: `username/repo`).

3. **Executing the Script**:
   - After configuring the environment variables in the `.env` file and setting up the repositories CSV, run the script with:
     ```bash
     python3 src/run_rm.py
     ```

4. **Script Behavior**:
   - The script clones each repository listed in the CSV file into the directory specified by `CLONE_DIR`, retrieves the default branch, and runs RefactoringMiner to analyze it.
   - Results and logs:
     - The analysis results from RefactoringMiner are saved as `.json` files in the `CLONE_DIR`.
     - Logs for each repository, including error messages, are saved as `.log` files in the same directory.

After execution, all repositories listed in the CSV will be processed, with results and logs saved in the configured directory.
