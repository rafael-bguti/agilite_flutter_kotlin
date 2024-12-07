import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(body: ResponsiveLayout()),
    );
  }
}

enum Layout { fixed, floating }

class ResponsiveLayout extends StatefulWidget {
  const ResponsiveLayout({super.key});

  @override
  State<ResponsiveLayout> createState() => _ResponsiveLayoutState();
}

const kSidebarWidth = 250.0;

class _ResponsiveLayoutState extends State<ResponsiveLayout> {
  bool fixedLayout = true;
  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(builder: (context, size) {
      if (size.maxWidth > 900 && !fixedLayout) {
        runOnNextBuild(() {
          setState(() {
            fixedLayout = true;
          });
        });
      } else if (size.maxWidth < 900 && fixedLayout) {
        runOnNextBuild(() {
          setState(() {
            fixedLayout = false;
          });
        });
      }

      return Stack(
        key: Key('DesktopLayout $fixedLayout'),
        children: [
          AnimatedPadding(
            key: const Key('DesktopBodyPadding'),
            duration: kThemeAnimationDuration,
            padding: EdgeInsets.only(left: fixedLayout ? kSidebarWidth : 0),
            child: const SizedBox(
              height: double.infinity,
              width: double.infinity,
              child: Center(
                child: Text('Body'),
              ),
            ),
          ),
          AnimatedPositioned(
            key: const Key('DesktopSideBar'),
            duration: kThemeAnimationDuration,
            curve: Curves.fastOutSlowIn,
            top: 0,
            bottom: 0,
            left: fixedLayout ? 0 : -kSidebarWidth,
            child: Container(
              width: kSidebarWidth,
              height: double.infinity,
              color: const Color(0xFFEAECEE),
              child: const Center(
                child: Text("Menu"),
              ),
            ),
          ),
        ],
      );
    });
  }
}
