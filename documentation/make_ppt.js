// Contact Manager with Fast Search (PS No. 73): presentation generator.
// Run:  npm install pptxgenjs   then   node make_ppt.js
// Keep rungta_logo.png in the same folder as this file. Output: Contact_Manager_PS73.pptx
const pptxgen = require("pptxgenjs"), fs = require("fs"), path = require("path");
const pres = new pptxgen();
pres.layout = "LAYOUT_16x9"; pres.title = "Contact Manager with Fast Search"; pres.author = "Yash Kumar Singh";
const INK = "000000", GREY = "555555", LIGHT = "EDEDED", W = "FFFFFF";
const LOGO = "image/png;base64," + fs.readFileSync(path.join(__dirname, "rungta_logo.png")).toString("base64");
const S = pres.shapes;
const T = (s, t, x, y, w, h, o = {}) => s.addText(t, Object.assign({ x, y, w, h, margin: 0, isTextBox: true, fontFace: "Arial", color: INK, fontSize: 12, valign: "top" }, o));
const B = (s, x, y, w, h, o = {}) => s.addShape(o.shape || S.RECTANGLE, { x, y, w, h, fill: { color: o.fill || W }, line: { color: INK, width: o.lw || 1 } });
const L = (s, x1, y1, x2, y2, o = {}) => s.addShape(S.LINE, { x: x1, y: y1, w: x2 - x1, h: y2 - y1, line: { color: INK, width: o.w || 1, endArrowType: o.a } });
const list = (a, fs) => a.map((t, i) => ({ text: t, options: { bullet: true, breakLine: i < a.length - 1, fontSize: fs || 12, paraSpaceAfter: 6 } }));
const tb = (s, rows, x, y, colW, o = {}) => s.addTable(rows.map((r, i) => r.map((c, j) => ({ text: c, options: {
  bold: i === 0 || j === 0, color: i === 0 ? W : INK, fill: { color: i === 0 ? INK : W }, fontSize: o.fs || 11,
  fontFace: o.mono && j === o.mono && i > 0 ? "Courier New" : "Arial", valign: "middle" } }))),
  { x, y, w: colW.reduce((a, b) => a + b, 0), colW, border: { type: "solid", pt: 1, color: INK }, margin: [0.07, 0.12, 0.07, 0.12] });
const notes = ["Hello everyone, I am Yash Kumar Singh. My project is a contact manager in Java that finds a phone number quickly.",
  "A contact book is only useful if you can find a number fast and never lose it. That is the problem I wanted to solve.",
  "These are the goals of the project, and what is inside the scope.",
  "The project is plain Java and needs nothing special to run.",
  "This is the flow of the program from startup to exit.",
  "Main handles the menu, ContactDirectory holds the three structures, Contact is one record.",
  "Each structure has one job. The list stores, the BST orders names, the HashMap finds phones.",
  "These are the eight things the program can do.",
  "These are the four assignment cases. All four passed in ProjectDemo.java.",
  "With more time, I would make the tree self-balancing and add partial-name search.",
  "These are the sources I used to learn and check the concepts.",
  "Thank you. I am happy to take questions."];
let n = 1;
function page(title, sub) {
  n++; const s = pres.addSlide(); s.background = { color: W }; s.addNotes(notes[n - 1]);
  T(s, title, 0.6, 0.35, 8.8, 0.5, { fontSize: 24, bold: true, fontFace: "Georgia" });
  T(s, sub, 0.6, 0.88, 8.8, 0.25, { fontSize: 11, italic: true, color: GREY });
  L(s, 0.6, 1.22, 9.4, 1.22, { w: 2 });
  L(s, 0.6, 5.12, 9.4, 5.12, { w: 0.5 });
  T(s, "CONTACT MANAGER WITH FAST SEARCH  |  JAVA · DSA PROJECT · PS NO. 73", 0.6, 5.22, 7, 0.2, { fontSize: 7, bold: true });
  T(s, String(n), 8.9, 5.22, 0.5, 0.2, { fontSize: 8, bold: true, align: "right" });
  return s;
}
function box(s, x, y, w, h, head, body, fs) {
  B(s, x, y, w, h);
  T(s, head, x + 0.2, y + 0.15, w - 0.4, 0.25, { fontSize: 11, bold: true, charSpacing: 1 });
  L(s, x + 0.2, y + 0.45, x + 0.7, y + 0.45, { w: 2 });
  T(s, body, x + 0.2, y + 0.58, w - 0.4, h - 0.7, { fontSize: fs || 11 });
}

// 1 Title
let s = pres.addSlide(); s.background = { color: W }; s.addNotes(notes[0]);
s.addImage({ data: LOGO, x: 7.7, y: 0.45, w: 1.7, h: 0.439 });
T(s, "JAVA  ·  DATA STRUCTURES AND ALGORITHMS  ·  PS NO. 73", 0.6, 1.2, 7, 0.25, { fontSize: 10, bold: true, charSpacing: 2 });
T(s, "CONTACT MANAGER\nWITH FAST SEARCH", 0.6, 1.65, 8.5, 1.5, { fontSize: 40, bold: true, fontFace: "Georgia" });
L(s, 0.6, 3.35, 2.6, 3.35, { w: 4 });
T(s, "A Java console directory with fast phone lookup", 0.6, 3.55, 8, 0.3, { fontSize: 15, color: GREY });
T(s, "Presented by: Yash Kumar Singh  |  ERP ID: RU-25-11630", 0.6, 4.3, 8, 0.25, { fontSize: 11, bold: true });
T(s, "Guide: Ms. Naina Singh  |  B.Tech CSE (AI & ML), Semester III  |  Rungta International Skills University", 0.6, 4.58, 8.8, 0.25, { fontSize: 10, color: GREY });

// 2 Introduction
s = page("INTRODUCTION", "Why a contact manager needs more than a simple list");
T(s, "A contact book is only useful if you can find a number quickly and never lose it.", 0.6, 1.45, 8.8, 0.7, { fontSize: 17, bold: true });
box(s, 0.6, 2.4, 2.8, 2.3, "THE PROBLEM", "Searching a long list one contact at a time is slow, and data is lost when the program closes.");
box(s, 3.6, 2.4, 2.8, 2.3, "THE SOLUTION", "A Java console directory to add, display, search, update, delete and sort contacts.");
box(s, 6.6, 2.4, 2.8, 2.3, "THE APPROACH", "A linked list stores contacts, a BST orders names, a HashMap finds phones, and contacts.txt saves them.");

// 3 Objectives and scope
s = page("OBJECTIVES AND SCOPE", "What the project sets out to do");
T(s, "OBJECTIVES", 0.6, 1.45, 4, 0.25, { fontSize: 12, bold: true, charSpacing: 1 });
T(s, list(["Store ID, name, phone and email", "Add, display, update and delete contacts", "Search by full name or by phone", "Show contacts in name order and sort the directory", "Reject duplicate and invalid phone numbers", "Keep contacts between runs"]), 0.6, 1.85, 4.3, 3.0);
L(s, 5.1, 1.5, 5.1, 4.8, { w: 0.5 });
T(s, "SCOPE", 5.3, 1.45, 4, 0.25, { fontSize: 12, bold: true, charSpacing: 1 });
T(s, list(["Menu-driven Java console program", "Phone numbers: exactly 10 digits, each one unique", "Data saved in contacts.txt", "Tested with the four cases of PS No. 73"]), 5.3, 1.85, 4.1, 3.0);

// 4 Technology
s = page("TECHNOLOGY AND REQUIREMENTS", "What is needed to build and run the program");
box(s, 0.6, 1.5, 4.3, 2.4, "SOFTWARE", list(["Java Development Kit (JDK)", "javac compiler and JVM", "Java standard library", "Data file: contacts.txt"]));
box(s, 5.1, 1.5, 4.3, 2.4, "HARDWARE", list(["Desktop or laptop", "Any system that can run Java"]));
T(s, "Core concepts:  BST  ·  Hashing  ·  Linked list  ·  Sorting  ·  File handling", 0.6, 4.3, 8.8, 0.3, { fontSize: 13, bold: true });

// 5 Methodology
s = page("METHODOLOGY", "How the program runs from start to exit");
[["Load", "Read contacts.txt at startup"], ["Choose", "Pick an option from the menu"], ["Validate", "Phone must be 10 digits and unique"], ["Update", "Change list, BST and HashMap together"], ["Save", "Option 8 writes contacts.txt"]].forEach((st, i) => {
  const cx = 1.4 + i * 1.8;
  s.addShape(S.OVAL, { x: cx - 0.3, y: 1.6, w: 0.6, h: 0.6, fill: { color: i % 2 ? W : INK }, line: { color: INK, width: 1.25 } });
  T(s, String(i + 1), cx - 0.3, 1.6, 0.6, 0.6, { fontSize: 16, bold: true, color: i % 2 ? INK : W, align: "center", valign: "middle" });
  if (i < 4) L(s, cx + 0.4, 1.9, cx + 1.4, 1.9, { a: "triangle" });
  T(s, st[0], cx - 0.8, 2.4, 1.6, 0.3, { fontSize: 14, bold: true, align: "center" });
  T(s, st[1], cx - 0.8, 2.75, 1.6, 0.7, { fontSize: 10, align: "center" });
});
B(s, 0.6, 3.8, 8.8, 0.8, { fill: LIGHT });
T(s, [{ text: "PROCESS   ", options: { bold: true } }, { text: "The menu repeats until the user chooses Save and exit. Then the records are written to contacts.txt." }], 0.85, 3.8, 8.3, 0.8, { fontSize: 12, valign: "middle" });

// 6 System design
s = page("SYSTEM DESIGN", "The files and what each one is responsible for");
box(s, 0.6, 1.5, 4.0, 1.5, "Main.java", "Console menu (options 1 to 8) and user input.");
box(s, 5.4, 1.5, 4.0, 1.5, "ContactDirectory.java", "Holds the linked list, BST and HashMap. Add, search, update, delete, sort and phone check.");
L(s, 4.6, 2.25, 5.4, 2.25, { a: "triangle", w: 1.5 });
box(s, 0.6, 3.3, 4.0, 1.5, "contacts.txt", "Loaded at startup and saved on exit. One line per contact.");
box(s, 5.4, 3.3, 4.0, 1.5, "Contact", "One record: ID, name, phone and email.");
L(s, 7.4, 3.0, 7.4, 3.3, { a: "triangle", w: 1.5 });
T(s, "calls", 4.65, 2.0, 0.7, 0.2, { fontSize: 8, italic: true, align: "center" });

// 7 DSA
s = page("DATA STRUCTURES AND ALGORITHMS", "One structure for each job");
box(s, 0.6, 1.5, 4.3, 1.6, "LINKED LIST", "Keeps contacts in the order they were added. Supplies the records when saving.");
box(s, 5.1, 1.5, 4.3, 1.6, "BINARY SEARCH TREE", "Ordered by name, ignoring case, ID breaks ties. In-order display: O(n). Search: O(log n) on average.");
box(s, 0.6, 3.3, 4.3, 1.6, "HASHMAP", "Maps a phone number to its contact. Lookup: O(1) on average.");
box(s, 5.1, 3.3, 4.3, 1.6, "INSERTION SORT", "Sorts a copy of the list by name, ascending or descending. Time: O(n²). The original order stays.");

// 8 Features
s = page("KEY FEATURES", "The eight things the program can do");
[["Add contact", "Creates a record with ID, name, phone and email."], ["Display", "Shows contacts in name order."], ["Search by name", "Finds a full name using the BST."], ["Search by phone", "Finds a number using the HashMap."],
 ["Update", "Changes name or phone and fixes the indexes."], ["Delete", "Removes it from the list, BST and HashMap."], ["Sort", "Ascending or descending, on a copy."], ["Save and load", "Load at start, save on exit."]].forEach((f, i) => {
  const x = 0.6 + (i % 4) * 2.25, y = 1.5 + Math.floor(i / 4) * 1.75;
  B(s, x, y, 2.05, 1.55);
  T(s, String(i + 1).padStart(2, "0"), x + 0.15, y + 0.12, 0.6, 0.3, { fontSize: 14, bold: true });
  T(s, f[0], x + 0.15, y + 0.5, 1.8, 0.3, { fontSize: 12, bold: true });
  T(s, f[1], x + 0.15, y + 0.82, 1.8, 0.65, { fontSize: 10, color: GREY });
});

// 9 Test cases
s = page("SAMPLE INPUT AND OUTPUT", "The four test cases of the project description");
tb(s, [["#", "Case", "Input or action", "Verified output"],
  ["1", "Normal input", "Add Aman with 9876543210", "Contact added: C101 | Aman | 9876543210"],
  ["2", "Boundary / minimum input", "Display the only contact", "C101 | Aman | 9876543210"],
  ["3", "Invalid input", "Search phone 9000000000", "Contact not found."],
  ["4", "Different valid scenario", "Add Riya and sort", "Ascending: C101 | Aman, then C102 | Riya\nDescending: C102 | Riya, then C101 | Aman"]],
  0.6, 1.5, [0.4, 2.2, 2.1, 4.1], { mono: 3, fs: 10.5 });
T(s, "All four cases passed through the console menu in ProjectDemo.java.", 0.6, 4.4, 8.8, 0.3, { fontSize: 12, bold: true });

// 10 Future and conclusion
s = page("FUTURE SCOPE AND CONCLUSION", "What can be improved and what the project achieved");
T(s, "FUTURE ENHANCEMENTS", 0.6, 1.45, 4, 0.25, { fontSize: 12, bold: true, charSpacing: 1 });
T(s, list(["Self-balancing tree for guaranteed O(log n) search", "Search by part of a name", "Email validation", "A graphical interface"]), 0.6, 1.85, 4.2, 2.5);
L(s, 5.1, 1.5, 5.1, 4.6, { w: 0.5 });
T(s, "CONCLUSION", 5.3, 1.45, 4, 0.25, { fontSize: 12, bold: true, charSpacing: 1 });
T(s, "The Contact Manager gives each job its own structure: the linked list for storage, the BST for name order and the HashMap for fast phone lookup. All three stay in step after every change, and contacts.txt keeps the data between runs.", 5.3, 1.85, 4.1, 2.8, { fontSize: 12 });

// 11 References
s = page("REFERENCES", "Sources used for learning and checking the concepts");
T(s, list(["Oracle Java Documentation: https://docs.oracle.com/en/java/", "GeeksforGeeks (BST, hashing, sorting): https://www.geeksforgeeks.org/", "W3Schools Java Tutorial: https://www.w3schools.com/java/", "Project description, PS No. 73, School of Computer Science and Engineering", "Class notes on Data Structures and Algorithms, Rungta International Skills University"], 13), 0.6, 1.6, 8.8, 3.2);

// 12 Thank you
s = pres.addSlide(); s.background = { color: INK }; s.addNotes(notes[11]);
T(s, "THANK YOU", 0.6, 1.9, 8.8, 0.9, { fontSize: 44, bold: true, fontFace: "Georgia", color: W, align: "center" });
T(s, "Questions and discussion", 0.6, 2.9, 8.8, 0.4, { fontSize: 16, color: "CCCCCC", align: "center" });
L(s, 4.4, 3.6, 5.6, 3.6, { w: 2 });
T(s, "CONTACT MANAGER WITH FAST SEARCH  ·  Yash Kumar Singh", 0.6, 3.85, 8.8, 0.3, { fontSize: 10, bold: true, color: W, align: "center" });

pres.writeFile({ fileName: path.join(__dirname, "Contact_Manager_PS73.pptx") }).then(() => console.log("ok"));
