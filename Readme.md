# Tree Visualizer
<img alt="GitHub file size in bytes" src="https://img.shields.io/github/size/Cyfine/TreeVisualizer/src/main/java/Visualizer.java">
<img alt="Dependencies" src="https://img.shields.io/badge/Dependencies-Processing-yellowgreen">
<img alt="Build Tool" src="https://img.shields.io/badge/Build%20Tool-Maven-orange">

During the Data Structure and Algorithms course, the Tree that implemented is not visible, which cause difficulties in debugging. This project implement a visualizer to real-time visualize the tree, make it easy to debug and learn the Tree structure.

## Getting Started


### Prerequisites
The Node class used by your tree should be exactly the same as the one in the Tree.java of this project.

The visualizer depends on Processing.core, add Processing.core to your build path manually or using Maven to build

If you are using Maven, add following to pom.xml
```
  <dependencies>
    <dependency>
        <groupId>org.processing</groupId>
        <artifactId>core</artifactId>
        <version>3.3.7</version>
    </dependency>
  </dependencies>
```



## Visualize your Binary Tree

Except the Node class, you do not need further modify your code. You can simply pass your Tree instance to the static fields in the Visualizer class.

### Call the Visualizer



```
Tree t = new Tree();
Visualizer.treeVisualize(t); 
```

### Sample Visualized Tree
![Screenshot](Sample.png)

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Processing](https://processing.org/) - External library




## Authors

* **Yifeng Carter CHENG** - *Initial work* - [Cyfine](https://github.com/Cyfine/)


## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details


