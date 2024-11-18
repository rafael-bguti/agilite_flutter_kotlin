import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(body: FullRebuildWidget()),
    );
  }
}

class FullRebuildWidget extends StatefulWidget {
  @override
  _FullRebuildWidgetState createState() => _FullRebuildWidgetState();
}

class _FullRebuildWidgetState extends State<FullRebuildWidget> {
  @override
  Widget build(BuildContext context) {
    final mq = MediaQuery.of(context);
    return MediaQuery(
      data: mq,
      child: Container(
        color: Colors.red,
        child: Center(
          child: Text(
            'Forçando reconstrução! ${mq.size.width}',
            style: TextStyle(color: Colors.white),
          ),
        ),
      ),
    );
  }
}
