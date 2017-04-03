#Visual Representation of a Red Black Tree

##Purpose

The purpose of this application is to visually illustrate what happens when a node in a red black tree is inserted,
deleted, or when a red black tree auto balances itself.

##Architecture

The application will be built around a node and tree class of the red black tree. The node class will define the
properties (color and number) and links each node has (parent and children). The tree class will define the properties
of a red black tree and will balance the tree as needed.

##Components

Backend

Nodes:
Must be objects with certain properties
Must have link to parent
Must have links to children, even if they are null leaves
When insertion or deletion happens, the tree must update all nodes to follow the properties of a red black tree
When the tree auto balances itself, the tree must also update these links

Frontend

User Interface:
Implementation of buttons and  text box
text box for number entry
button for insertion
button for deletion
Optional(?):
Step forward feature
Step backward feature

Nodes:
Show proper colors of the node, especially when nodes must change color
Show links to parent and children (including null children)
Update links when rebalance, insertion, or deletion occurs
Optional(?):
When searching through a tree, implement a feature to indicate where the tree currently is
Dialogue to walk through the process of what the tree is currently doing

