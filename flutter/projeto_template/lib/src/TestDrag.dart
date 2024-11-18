// main.dart

import 'package:flutter/material.dart';

void main() => runApp(DragDropExample());

class DragDropExample extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Exemplo de Drag and Drop',
      home: DragDropPage(),
    );
  }
}

class DragDropPage extends StatefulWidget {
  @override
  _DragDropPageState createState() => _DragDropPageState();
}

class _DragDropPageState extends State<DragDropPage> {
  bool _isIconInFirstContainer = true;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Exemplo de Drag and Drop'),
      ),
      body: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          buildDragTarget(
            isIconPresent: _isIconInFirstContainer,
            onAccept: (data) {
              setState(() {
                _isIconInFirstContainer = true;
              });
            },
          ),
          buildDragTarget(
            isIconPresent: !_isIconInFirstContainer,
            onAccept: (data) {
              setState(() {
                _isIconInFirstContainer = false;
              });
            },
          ),
        ],
      ),
    );
  }

  Widget buildDragTarget({bool isIconPresent = false, Function(String)? onAccept}) {
    return DragTarget<String>(
      onWillAccept: (data) => true,
      onAccept: onAccept,
      builder: (context, candidateData, rejectedData) {
        return Container(
          width: 150,
          height: 150,
          color: Colors.blue[100],
          child: Center(
            child: isIconPresent
                ? Draggable<String>(
                    data: 'icon',
                    feedback: Icon(
                      Icons.star,
                      color: Colors.orange,
                      size: 50,
                    ),
                    childWhenDragging: Container(),
                    child: Icon(
                      Icons.star,
                      color: Colors.orange,
                      size: 50,
                    ),
                  )
                : Container(),
          ),
        );
      },
    );
  }
}
