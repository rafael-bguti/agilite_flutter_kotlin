import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class PageNotFound extends StatelessWidget {
  const PageNotFound({super.key});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: ASpacingColumn(
        spacing: 32,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            "404 - Tarefa não localizada",
            style: textTheme?.labelLarge?.copyWith(color: errorColor),
          ),
          FilledButton.icon(
            onPressed: () {
              ANavigator.replace(dashboardPath);
            },
            label: const Text("Voltar para o início"),
            icon: const Icon(Icons.home),
          ),
        ],
      ),
    );
  }
}
