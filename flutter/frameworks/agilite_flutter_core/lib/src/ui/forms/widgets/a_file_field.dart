import 'package:agilite_flutter_core/core.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';

class AFileField extends StatefulWidget {
  final FileController controller;

  //FliePicker values
  final FileType? fileType;
  final List<String>? allowedExtensions;

  final String? labelText;
  final String? hintText;
  final String? helperText;

  final InputDecoration localDecoration;
  final TextInputAction? textInputAction;

  final bool enabled;

  const AFileField({
    required this.controller,
    this.labelText,
    this.hintText,
    this.helperText,
    this.enabled = true,
    this.textInputAction,
    this.fileType,
    this.allowedExtensions,
    InputDecoration? decoration,
    super.key,
  }) : localDecoration = decoration ?? const InputDecoration();

  @override
  State<AFileField> createState() => _AFileFieldState();
}

class _AFileFieldState extends State<AFileField> {
  late final FileController fileController = widget.controller;

  @override
  Widget build(BuildContext context) {
    return AConsumer(
      notifier: fileController,
      builder: (context, fieldController, _) {
        return TextFormField(
          decoration: widget.localDecoration.copyWith(
            floatingLabelBehavior: FloatingLabelBehavior.always,
            errorText: fileController.errorText,
            errorMaxLines: 2,
            suffixIcon: _buildSuffixIcon(),
            labelText: widget.localDecoration.labelText ?? widget.labelText,
            hintText: widget.localDecoration.hintText ?? widget.hintText,
            hintStyle: widget.localDecoration.hintStyle ?? const TextStyle(color: Color(0xFFBABABA)), //TODO: DarkMode - ver cor para o dark mode
            helperText: widget.localDecoration.helperText ?? widget.helperText,
          ),
          enabled: widget.enabled ?? true,
          textAlignVertical: TextAlignVertical.center,
          textAlign: TextAlign.start,
          controller: fileController.textController,
          textInputAction: widget.textInputAction,
          readOnly: true,
          onTap: () {
            _pickFile(context);
          },
        );
      },
    );
  }

  Widget _buildSuffixIcon() {
    return Padding(
      padding: const EdgeInsets.all(2.0),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          if (fileController.textController.text.isNotEmpty)
            IconButton(
              icon: const Icon(Icons.clear, color: Colors.red),
              onPressed: () {
                fileController.clear();
              },
            ),
          FilledButton.tonal(
            style: TextButton.styleFrom(
              shape: const RoundedRectangleBorder(
                borderRadius: BorderRadius.all(Radius.circular(4)),
              ),
            ),
            onPressed: widget.enabled ? () => _pickFile(context) : null,
            child: const Text('Selecionar arquivo'),
          ),
        ],
      ),
    );
  }

  Future<void> _pickFile(BuildContext context) async {
    final result = await FilePicker.platform.pickFiles(
      type: widget.fileType ?? FileType.any,
      allowedExtensions: widget.allowedExtensions,
    );
    if (result == null) {
      return;
    }
    fileController.onSelectFile(result);
  }
}
