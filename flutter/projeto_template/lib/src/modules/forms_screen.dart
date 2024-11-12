import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class FormsScreen extends StatelessWidget {
  const FormsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: AContainer(taskHeader: "Forms", child: Placeholder()),
    );
  }
}
