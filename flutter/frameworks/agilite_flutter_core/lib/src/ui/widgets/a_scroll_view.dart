// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:math';

import 'package:flutter/material.dart';

class AScrollView extends StatefulWidget {
  final Widget? header;
  final bool addHorizontalScroll;
  final Widget body;
  final AScrollController? scrollController;

  const AScrollView({
    Key? key,
    required this.body,
    this.scrollController,
    this.addHorizontalScroll = false,
    this.header,
  }) : super(key: key);

  @override
  State<AScrollView> createState() => _ScrollViewState();
}

class _ScrollViewState extends State<AScrollView> {
  late final AScrollController _scrollController = widget.scrollController ?? AScrollController._();

  @override
  void initState() {
    super.initState();
    _scrollController.init(_hasHeader);
  }

  @override
  void dispose() {
    if (_scrollController._disposeOnViewDispose) {
      _scrollController.dispose();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (_hasHeader) {
      return Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          _buildHeader(),
          Expanded(
            child: _buildBody(),
          ),
        ],
      );
    } else {
      return _buildBody();
    }
  }

  Widget _buildHeader() {
    return Scrollbar(
      thickness: 0,
      trackVisibility: false,
      thumbVisibility: false,
      controller: _scrollController.headerHorizontal,
      notificationPredicate: (notif) => false,
      child: _wrapperInHorizontalScroll(
        widget.header!,
        _scrollController.headerHorizontal,
        const NeverScrollableScrollPhysics(),
      ),
    );
  }

  Widget _buildBody() {
    return Scrollbar(
      controller: _scrollController.bodyVertical,
      thumbVisibility: true,
      child: Scrollbar(
        controller: _scrollController.bodyHorizontal,
        thumbVisibility: widget.addHorizontalScroll,
        notificationPredicate: (notif) => notif.depth == 1,
        child: SingleChildScrollView(
          controller: _scrollController.bodyVertical,
          child: _wrapperInHorizontalScroll(
            widget.body,
            _scrollController.bodyHorizontal,
          ),
        ),
      ),
    );
  }

  Widget _wrapperInHorizontalScroll(Widget child, ScrollController controller, [ScrollPhysics? physics]) {
    if (!widget.addHorizontalScroll) return child;
    return SingleChildScrollView(
      controller: controller,
      physics: physics,
      scrollDirection: Axis.horizontal,
      child: child,
    );
  }

  bool get _hasHeader => widget.header != null;
}

class AScrollController {
  final double offSet = 55;
  final ScrollController headerHorizontal = ScrollController(debugLabel: 'AScrollVisible');
  final ScrollController bodyHorizontal = ScrollController(debugLabel: 'AScrollVisible');
  final ScrollController bodyVertical = ScrollController(debugLabel: 'AScrollVisible');

  final bool _disposeOnViewDispose;

  AScrollController() : _disposeOnViewDispose = false;
  AScrollController._() : _disposeOnViewDispose = true;

  void init(bool hasHeader) {
    if (hasHeader) {
      bodyHorizontal.addListener(() {
        headerHorizontal.jumpTo(bodyHorizontal.offset);
      });
    }
  }

  void dispose() {
    headerHorizontal.dispose();
    bodyHorizontal.dispose();
    bodyVertical.dispose();
  }

  void moveScrollTo(Rectangle<double> rect) {
    move(bodyVertical, rect.top, rect.top + rect.height);
    move(bodyHorizontal, rect.left, rect.left + rect.width);
  }

  void move(ScrollController controller, double startPosition, double endPosition) {
    if (!controller.hasClients) return;

    startPosition = startPosition - offSet;
    endPosition = endPosition + offSet;

    final currentScroll = controller.position.pixels;
    final viewport = controller.position.viewportDimension;
    final endScroll = currentScroll + viewport;

    bool isCompleteVisible = startPosition >= currentScroll && endPosition <= endScroll;
    if (isCompleteVisible) return;

    var newPosition = 0.0;
    if (endPosition > endScroll) {
      newPosition = endPosition - viewport;
    } else if (startPosition < currentScroll) {
      newPosition = startPosition;
    }

    final maxScroll = controller.position.maxScrollExtent;
    controller.jumpTo(max(min(newPosition, maxScroll), 0));
  }
}
