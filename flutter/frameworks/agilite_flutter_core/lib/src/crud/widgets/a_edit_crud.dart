import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import 'edit_crud_buttons.dart';

class AEditCrud extends StatefulWidget {
  final String taskName;
  final CrudDescr descr;
  final int? id;
  final Widget formBody;

  const AEditCrud({
    required this.taskName,
    required this.formBody,
    required this.descr,
    this.id,
    super.key,
  });

  @override
  State<AEditCrud> createState() => _AEditCrudState();
}

class _AEditCrudState extends State<AEditCrud> {
  late final CrudEditController controller = CrudEditController(
    taskName: widget.taskName,
    id: widget.id,
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
              onSave: controller.save,
            ),
            child: AForm(
              controller.formController,
              value: state is CrudEditEditingState ? state.data : null,
              child: widget.formBody,
            ),
          );
        },
      ),
    );
  }
}
