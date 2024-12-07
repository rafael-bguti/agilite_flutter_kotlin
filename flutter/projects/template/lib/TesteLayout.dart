import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: ResponsiveLayout(),
    );
  }
}

class ResponsiveLayout extends StatefulWidget {
  const ResponsiveLayout({super.key});

  @override
  _ResponsiveLayoutState createState() => _ResponsiveLayoutState();
}

class _ResponsiveLayoutState extends State<ResponsiveLayout> {
  bool isWideScreen = false;

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        bool newIsWideScreen = constraints.maxWidth > 900;
        if (newIsWideScreen != isWideScreen) {
          runOnNextBuild(() {
            setState(() {
              isWideScreen = newIsWideScreen;
            });
          });
        }

        return Scaffold(
          body: Row(
            children: [
              AnimatedContainer(
                duration: const Duration(milliseconds: 300),
                width: isWideScreen ? 250 : 0,
                child: isWideScreen ? NavigationDrawer() : null,
              ),
              Expanded(
                child: Column(
                  children: [
                    AppBar(
                      title: const Text('Responsive Layout'),
                      backgroundColor: Colors.teal,
                    ),
                    const Expanded(
                      child: Center(
                        child: Text('Main Content'),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
          drawer: AnimatedContainer(
            duration: const Duration(milliseconds: 300),
            width: isWideScreen ? 0 : 250,
            child: NavigationDrawer(),
          ),
        );
      },
    );
  }
}

class NavigationDrawer extends StatelessWidget {
  const NavigationDrawer({super.key});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      shape: const ContinuousRectangleBorder(),
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          const DrawerHeader(
            decoration: BoxDecoration(
              color: Colors.blue,
            ),
            child: Text('Menu'),
          ),
          ListTile(
            title: const Text('Item 1'),
            onTap: () {
              // Handle the tap
            },
          ),
          ListTile(
            title: const Text('Item 2'),
            onTap: () {
              // Handle the tap
            },
          ),
        ],
      ),
    );
  }
}
