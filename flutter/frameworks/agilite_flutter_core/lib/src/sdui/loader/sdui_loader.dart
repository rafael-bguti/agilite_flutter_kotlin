import 'dart:async';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import '../sdui_context.dart';
import '../sdui_render.dart';

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

  final sduiContext = SduiContext();

  @override
  void dispose() {
    controller.dispose();
    sduiContext.dispose();
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
          sduiContext,
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
    FutureOr<String?> contentOr = provider.getContent();
    if (contentOr is Future) {
      showLoading("Carregando dados da tarfa");

      String? content = await contentOr;
      state = _ViewState(sduiJson: content?.toMap());
    } else {
      state = _ViewState(sduiJson: contentOr.toMap());
    }
  }
}

class _ViewState {
  final Map<String, dynamic>? sduiJson;

  _ViewState({
    required this.sduiJson,
  });
}
