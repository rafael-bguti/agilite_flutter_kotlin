import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ASearchField extends StatefulWidget {
  final TextEditingController? searchController;
  final void Function(String) onSearchChanged;
  final String hintText;

  const ASearchField({
    super.key,
    required this.onSearchChanged,
    this.hintText = 'Pesquisar',
    this.searchController,
  });

  @override
  State<ASearchField> createState() => _ASearchFieldState();
}

class _ASearchFieldState extends State<ASearchField> {
  final _debounceTimer = DebounceTimer();
  late final TextEditingController searchController;
  late final bool _isControllerProvided;

  void _onSearchChanged() {
    setState(() {});
    _debounceTimer.run(() {
      widget.onSearchChanged(searchController.text);
    });
  }

  @override
  void initState() {
    super.initState();

    if (widget.searchController != null) {
      searchController = widget.searchController!;
      _isControllerProvided = true;
    } else {
      searchController = TextEditingController();
      _isControllerProvided = false;
    }
  }

  @override
  void dispose() {
    if (!_isControllerProvided) searchController.dispose();

    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return TextField(
      controller: searchController,
      decoration: InputDecoration(
        hintText: widget.hintText,
        prefixIcon: const Icon(Icons.search),
        suffixIcon: _buildSuffixIcon(),
      ),
      onChanged: (_) => _onSearchChanged(),
    );
  }

  Widget? _buildSuffixIcon() {
    if (searchController.text.isEmpty) return const SizedBox.shrink();
    return IconButton(
      icon: const Icon(Icons.clear),
      onPressed: () {
        searchController.text = '';
        widget.onSearchChanged('');
      },
    );
  }
}
