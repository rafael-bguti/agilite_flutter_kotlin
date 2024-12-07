import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ASearchField extends StatefulWidget {
  final String name;

  final void Function(String?) onSearchChanged;
  final String hintText;

  const ASearchField(
    this.name, {
    super.key,
    required this.onSearchChanged,
    this.hintText = 'Pesquisar',
  });

  @override
  State<ASearchField> createState() => _ASearchFieldState();
}

class _ASearchFieldState extends State<ASearchField> {
  final _debounceTimer = DebounceTimer();
  late final TextEditingController searchController;

  void _onSearchChanged(String? value) {
    setState(() {});
    _debounceTimer.run(() {
      widget.onSearchChanged(value);
    });
  }

  @override
  void initState() {
    super.initState();

    searchController = TextEditingController();
  }

  @override
  void dispose() {
    searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return ATextField.string(
      widget.name,
      textEditingController: searchController,
      decoration: InputDecoration(
        hintText: widget.hintText,
        prefixIcon: const Icon(Icons.search),
        suffixIcon: _buildSuffixIcon(),
      ),
      onChanged: (v) => _onSearchChanged(v),
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
