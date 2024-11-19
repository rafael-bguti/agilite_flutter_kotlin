import 'dart:async';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ABottomDialogQuestion extends StatefulWidget {
  final String message;
  final Future<void> Function(BuildContext context) onConfirm;
  final void Function()? onCancel;
  final IconData icon;
  final String confirmButtonText;
  final Color? primary;
  final bool popOnConfirm;
  const ABottomDialogQuestion({
    required this.message,
    required this.onConfirm,
    this.onCancel,
    this.icon = Icons.delete_outline_rounded,
    this.confirmButtonText = 'Sim',
    this.primary,
    this.popOnConfirm = true,
    super.key,
  });

  @override
  State<ABottomDialogQuestion> createState() => _ABottomDialogQuestionState();
}

class _ABottomDialogQuestionState extends State<ABottomDialogQuestion> {
  late final Color color = widget.primary ?? successColor;

  bool running = false;
  @override
  Widget build(BuildContext context) {
    return Container(
      constraints: BoxConstraints(
        minWidth: MediaQuery.of(context).size.width,
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Expanded(
            child: Container(
              color: colorScheme?.surfaceContainerLow,
              child: Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(
                      widget.icon,
                      size: 38,
                      color: color,
                    ),
                    const SizedBox(height: 18),
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text(
                        widget.message,
                        style: textTheme?.headlineSmall!.copyWith(
                          color: color,
                        ),
                        textAlign: TextAlign.center,
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
          const ADivider.lineOnly(),
          Expanded(
            child: Container(
              color: colorScheme?.surfaceContainerHighest,
              child: Center(
                child: running
                    ? Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Container(
                            constraints: const BoxConstraints(
                              maxWidth: 300,
                            ),
                            child: const LinearProgressIndicator(),
                          ),
                          Text("Aguarde...", style: textTheme?.bodyLarge),
                        ],
                      )
                    : ASpacingRow(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          ElevatedButton(
                            onPressed: () {
                              Navigator.of(context).pop();
                              widget.onCancel?.call();
                            },
                            child: Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8),
                              child: Text('Não', style: textTheme?.titleLarge),
                            ),
                          ),
                          const SizedBox(width: 18),
                          ElevatedButton(
                            style: ElevatedButton.styleFrom(backgroundColor: color),
                            onPressed: () => _doConfirm(context),
                            child: Padding(
                              padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8),
                              child: Text(
                                widget.confirmButtonText,
                                style: textTheme?.titleLarge?.copyWith(
                                  color: Colors.white,
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  void _doConfirm(BuildContext context) async {
    try {
      setState(() {
        running = true;
      });
      await widget.onConfirm(context);
    } finally {
      if (widget.popOnConfirm && context.mounted) {
        Navigator.of(context).pop();
      }
      setState(() {
        running = false;
      });
    }
  }
}
