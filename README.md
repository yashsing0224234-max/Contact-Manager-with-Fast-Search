# Contact Manager with Fast Search

**Data Structures project · PS No. 73 · Java 21**

A console directory that stores a name, a unique 10-digit phone number and an optional email for each contact. You can find everyone with the same name, look up one person by phone, update or delete a record, and print the directory in either name order.

## Project files

| Path | What it contains |
| --- | --- |
| [codebase](codebase/) | Java source, including the menu in `Main.java` and the four assignment tests in `ProjectDemo.java` |
| [contacts.txt](codebase/contacts.txt) | Two sample records, Aman and Riya. The program loads and saves this file. |
| [Project report](documentation/Contact_Manager_Interim_Report.docx) | Project design, flowchart, class diagram, complexity and test results |
| [Presentation](documentation/Contact_Manager_PS73.pptx) | PowerPoint slides with worked examples and speaker notes |

## Run the project

Install JDK 21 and open a terminal in the repository folder. These commands work in PowerShell, Command Prompt, macOS and Linux.

```sh
cd codebase
javac -encoding UTF-8 -d out -sourcepath . Main.java ProjectDemo.java
java -cp out Main
```

Start with option **4** and enter `9876543210` to find Aman in the sample file. Use option **8** to save and exit. Run the program from `codebase` so it uses the supplied `contacts.txt`.

## Menu

| Option | Action | Option | Action |
| --- | --- | --- | --- |
| 1 | Add a contact | 5 | Update a contact by its current phone |
| 2 | Display contacts in name order | 6 | Delete a contact by phone |
| 3 | Search by full name, ignoring case | 7 | Sort by name. Enter or `A` selects ascending order and `D` selects descending. |
| 4 | Search by phone | 8 | Save and exit |

## Data structures

`ContactList` keeps the contacts in insertion order. `NameBST` indexes contacts by name and breaks ties using the numeric ID. A `HashMap<String, Contact>` in `ContactDirectory` supports phone lookups in O(1) time on average. Both indexes refer to the same contact object. For a sorted report, `ContactDirectory` runs insertion sort on a copy of the list.

`ContactFile` reads and writes UTF-8 rows in the order `id,name,phone,email`, with no header. Phone numbers are stored as text so a leading zero is preserved. Each run loads the saved records and rebuilds the indexes.

## Run the four assignment tests

After compiling, run this command from `codebase`.

```sh
java -cp out ProjectDemo
```

The tests use their own temporary `contacts.txt`. They add Aman, display a one-contact directory, search for a missing number, then add Riya and check both sort directions. A successful run ends with `4 of 4 cases passed.`
