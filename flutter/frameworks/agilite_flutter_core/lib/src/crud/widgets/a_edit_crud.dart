import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import 'edit_crud_buttons.dart';

class AEditCrud extends StatefulWidget {
  final String taskName;
  final int? id;
  final CrudDescr descr;
  final Map<String, dynamic> data;
  final Widget formBody;

  const AEditCrud({
    required this.taskName,
    required this.id,
    required this.formBody,
    required this.descr,
    required this.data,
    super.key,
  });

  @override
  State<AEditCrud> createState() => _AEditCrudState();
}

class _AEditCrudState extends State<AEditCrud> {
  late final CrudEditController controller = CrudEditController(
    taskName: widget.taskName,
  );

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: AView(
        controller: controller,
        builder: (context, state) {
          return ATaskContainer(
            header: AContainerHeader.text(
              '${widget.id == null ? "Incluindo" : "Editando"} - ${widget.descr.singular}',
            ),
            footer: EditCrudButtons(
              onSave: () => controller.save(widget.id),
            ),
            child: AForm(
              controller.formController,
              value: widget.data,
              child: widget.formBody,
            ),
          );
        },
      ),
    );
  }
}
