import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ClientesScreen extends StatelessWidget {
  const ClientesScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: ElevatedButton(
        child: const Text("Dashboard"),
        onPressed: () {
          fullLayoutFacade.selectMenuItemAndNavigationReplace('/dashboard');
        },
      ),
    );
  }
}
