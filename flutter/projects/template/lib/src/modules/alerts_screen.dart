import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AlertsScreen extends StatelessWidget {
  const AlertsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return ATaskContainer(
      header: const AContainerHeader.text("Alerts"),
      child: ASpacingColumn(
        spacing: 32,
        children: [
          _Grid(
            label: "Light",
            child: ASpacingColumn(
              children: [
                AAlert.info(message: "This is a Info alert", renderType: RenderType.light),
                AAlert.success(message: "This is a success alert", renderType: RenderType.light),
                AAlert.warning(message: "This is a warning alert", renderType: RenderType.light),
                AAlert.error(message: "This is a error alert", renderType: RenderType.light),
              ],
            ),
          ),
          _Grid(
            label: "Soft background",
            child: ASpacingColumn(
              children: [
                AAlert.info(message: "This is a Info alert"),
                AAlert.success(message: "This is a success alert"),
                AAlert.warning(message: "This is a warning alert"),
                AAlert.error(message: "This is a error alert"),
              ],
            ),
          ),
          _Grid(
            label: "Strong background",
            child: ASpacingColumn(
              children: [
                AAlert.info(message: "This is a Info alert", renderType: RenderType.strong),
                AAlert.success(message: "This is a success alert", renderType: RenderType.strong),
                AAlert.warning(message: "This is a warning alert", renderType: RenderType.strong),
                AAlert.error(message: "This is a error alert", renderType: RenderType.strong),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _Grid extends StatelessWidget {
  final String label;
  final Widget child;
  const _Grid({
    required this.label,
    required this.child,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return AGrid(
      areas: const ['4, 8'],
      children: [
        ADivider.text(text: label),
        Padding(
          padding: const EdgeInsets.only(top: 32.0),
          child: child,
        ),
      ],
    );
  }
}
