# Chmod Buddy

Do you always forget which `chmod` command to use? Are Linux file permissions a constant source of confusion? Do you find yourself staring at terminal output, trying to decode whether you can read, write, or execute a file?

Well, worry no more—**Chmod Buddy** is here for you! Say goodbye to the guesswork and frustration of managing file permissions. With Chmod Buddy, you get a simple, user-friendly interface that takes the hassle out of setting file permissions. Whether you’re an experienced user or new to Linux, Chmod Buddy lets you configure permissions with just a few clicks—and instantly generates the correct `chmod` command.

Tired of typing out confusing terminal commands? Just paste your `ls -l` output into Chmod Buddy, and it will decode the permissions for you in seconds! Clear, simple, and effective. Chmod Buddy makes handling Linux file permissions a breeze.

## Features

- **Graphical Permissions Configuration**: Select read, write, and execute permissions for the owner, group, and others using checkboxes.
- **Automatic `chmod` Command Generation**: The app generates the appropriate `chmod` command based on your selected permissions.
- **Parse `ls -l` Output**: Paste in the output from the `ls -l` command, and the app will parse and display detailed information about file type, permissions, owner, group, and more.
- **Clear Button**: Easily reset all fields and start fresh with the click of a button.
- **Cross-Platform**: Works on macOS (packaged as an app) and other platforms via the JAR file.

## How to Use

1. **Download and run the application** (either the JAR or the packaged macOS app).
2. **Configure Permissions**: Use the checkboxes to select read, write, and execute permissions for the owner, group, and others.
3. **Generate `chmod` Command**: Click "Generate `chmod` Command" to see the appropriate `chmod` command generated in the command field.
4. **Paste `ls -l` Output**: You can also paste the output from the `ls -l` command into the input field, and the app will parse and display detailed information, updating the checkboxes accordingly.
5. **Clear Fields**: Use the "Clear" button to reset everything and start over.

## Requirements

- **Java 8 or higher** is required to run the JAR file.
- On macOS, the app is available as a packaged `.app`.

## Installation

- **For macOS**: Download the `.app` file from the releases page and drag it into your Applications folder.
- **For other platforms**: Download the JAR file and run it using the command:

    ```bash
    java -jar ChmodBuddy.jar
    ```

## Screenshots

_TODO: Add screenshots of the application here._

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
