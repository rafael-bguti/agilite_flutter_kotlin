import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

enum AlertType {
  warning,
  error,
  info,
  success,
}

class AAlert extends StatelessWidget {
  final String message;
  final String title;

  final AlertType type;

  const AAlert._(
    this.message,
    this.type,
    this.title, {
    super.key,
  });

  const AAlert.warning({
    required this.message,
    this.title = 'Atenção',
    super.key,
  }) : type = AlertType.warning;

  const AAlert.error({
    required this.message,
    this.title = 'Erro',
    super.key,
  }) : type = AlertType.error;

  const AAlert.info({
    required this.message,
    this.title = 'Informação',
    super.key,
  }) : type = AlertType.info;

  const AAlert.success({
    required this.message,
    this.title = 'Sucesso',
    super.key,
  }) : type = AlertType.success;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(8),
      decoration: BoxDecoration(
        color: _getBackColor().withOpacity(0.6),
        borderRadius: BorderRadius.circular(5),
        border: Border.all(
          color: _getBackColor(),
          width: 1,
        ),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(
            _getIcon(),
            color: _getForeColor(),
          ),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  title,
                  style: TextStyle(
                    color: _getForeColor(),
                    fontWeight: FontWeight.bold,
                  ),
                ),
                SelectableText(
                  message,
                  style: TextStyle(
                    color: _getForeColor(),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  IconData _getIcon() {
    switch (type) {
      case AlertType.warning:
        return Icons.warning;
      case AlertType.error:
        return Icons.error;
      case AlertType.info:
        return Icons.info;
      case AlertType.success:
        return Icons.check_circle;
      default:
        return Icons.info;
    }
  }

  Color _getBackColor() {
    switch (type) {
      case AlertType.warning:
        return warningColor;
      case AlertType.error:
        return errorColor;
      case AlertType.info:
        return primaryColor;
      case AlertType.success:
        return successColor;
      default:
        return Colors.transparent;
    }
  }

  Color _getForeColor() {
    switch (type) {
      case AlertType.warning:
        return onWarningColor;
      case AlertType.error:
        return onErrorColor;
      case AlertType.info:
        return onPrimaryColor;
      case AlertType.success:
        return onSuccessColor;
      default:
        return Colors.transparent;
    }
  }
}
