import 'dart:async';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class SduiLoader extends StatefulWidget {
  final SduiContentProvider contentProvider;
  const SduiLoader({
    required this.contentProvider,
    super.key,
  });

  @override
  State<SduiLoader> createState() => _SduiLoaderState();
}

class _SduiLoaderState extends State<SduiLoader> {
  late final controller = _SduiController(
    provider: widget.contentProvider,
  );

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AView(
      controller: controller,
      builder: (context, state) {
        if (state.sduiJson == null) {
          return const PageNotFound();
        }
        return SduiRender.renderFromJson(
          context,
          state.sduiJson!,
        );
      },
    );
  }
}

class _SduiController extends ViewController<_ViewState> {
  final SduiContentProvider provider;

  _SduiController({
    required this.provider,
  });

  @override
  Future<void> onViewLoaded() async {
    FutureOr<Map<String, dynamic>?> contentOr = provider.getContent();
    if (contentOr is Future) {
      showLoading("Carregando dados da tarefa");

      Map<String, dynamic>? content = await contentOr;
      state = _ViewState(sduiJson: content);
    } else {
      state = _ViewState(sduiJson: contentOr);
    }
  }
}

class _ViewState {
  final Map<String, dynamic>? sduiJson;

  _ViewState({
    required this.sduiJson,
  });
}
