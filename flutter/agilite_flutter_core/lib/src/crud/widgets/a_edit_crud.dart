import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import 'a_edit_crud_buttons.dart';

class AEditCrud extends StatefulWidget {
  final CrudDescr descr;
  final int? id;
  final Widget formBody;

  const AEditCrud({
    required this.formBody,
    required this.descr,
    this.id,
    super.key,
  });

  @override
  State<AEditCrud> createState() => _AEditCrudState();
}

class _AEditCrudState extends State<AEditCrud> {
  late final CrudEditController controller = CrudEditController(id: widget.id);

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
          return AContainer(
            header: AContainerHeader.text('${widget.id == null ? "Incluindo" : "Editando"} - ${widget.descr.singular}'),
            footer: SizedBox(
              height: 60,
              child: AEditCrudButtons(
                onSave: controller.save,
              ),
            ),
            child: SingleChildScrollView(
              child: AForm(
                controller.formController,
                child: widget.formBody,
              ),
            ),
          );
        },
      ),
    );
  }
}
