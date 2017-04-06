# Frontend design
# Basic
* GraphicNode: represents a node on the Graphic
* Node: represents a node on the RB tree
  (every Node may have a field of GraphicNode that coincide its graphic representation)

* Animation process:
  1. The frontend get the user input and call method in RBTree to do the operation
  2. After the operation done, the backend return all the **sequencial** information the frontend needed for animation
  3. The frontend will process the info **sequencially** and create each step an animation, and after done, all the animation
created will be schedule to one **Sequential Transition**, and then play the animation

# TO-DO
- [ ] Node tranversal
- [ ] Adjust the tree size so that it can have more space for next node
- [ ] Solely add child node to a specific tree
- [ ] Color swapping
- [ ] 4 types of rotation
- [ ] deletion?


## Illustration
![Illustration](http://i.imgur.com/HL58rIB.png)
