import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool isDarkMode = false; // Estado do tema

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: isDarkMode ? ThemeData.dark() : ThemeData.light(), // Altera o tema com base no estado
      home: Scaffold(
        appBar: AppBar(
          title: Text('Troca de Tema'),
        ),
        body: Center(
          child: LayoutBuilder(
            builder: (context, constraints) {
              int crossAxisCount = constraints.maxWidth > 600 ? 4 : 2;

              return GridView.count(
                crossAxisCount: crossAxisCount,
                crossAxisSpacing: 2.0,
                children: List.generate(20, (index) {
                  return Container(
                    margin: EdgeInsets.all(8.0),
                    color: Colors.orange,
                    height: 100,
                    child: Center(child: Text('Item $index')),
                  );
                }),
              );
            },
          ),
        ),
      ),
    );
  }
}
