import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

const _customTabs = ['Traffic', 'Purchcases', 'Archived'];

class TabsScreen extends StatefulWidget {
  const TabsScreen({super.key});

  @override
  State<TabsScreen> createState() => _TabsScreenState();
}

class _TabsScreenState extends State<TabsScreen> {
  final ValueNotifier<int> _$customSelectedTab = ValueNotifier(0);

  @override
  void dispose() {
    _$customSelectedTab.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: AContainer(
        headerLabel: "Tabs",
        child: ASpacingColumn(
          children: [
            AGrid(
              spacing: 16,
              areas: const [
                '4, 8',
              ],
              children: [
                const ADivider.text(text: "Custom"),
                Padding(
                  padding: const EdgeInsets.only(top: 24.0),
                  child: ACard(
                    padding: EdgeInsets.zero,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        ATabHeader.builder(
                          headerCount: _customTabs.length,
                          contentBuilder: _buildCustomTab,
                          onTabChanged: (index) {
                            _$customSelectedTab.value = index;
                          },
                        ),
                        AConsumer(
                          notifier: _$customSelectedTab,
                          builder: (_, __, ___) {
                            return Padding(
                              padding: EdgeInsets.all(8.0),
                              child: Center(
                                child: Text('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec turpis. = ${_$customSelectedTab.value}'),
                              ),
                            );
                          },
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
            AGrid(
              spacing: 16,
              areas: const [
                '4, 8',
              ],
              children: [
                const ADivider.text(text: "Underlined"),
                Padding(
                  padding: const EdgeInsets.only(top: 24.0),
                  child: ACard(
                    padding: EdgeInsets.zero,
                    child: Column(
                      children: [
                        ATabHeader.text(
                          headerTexts: const ['Traffic', 'Purchases', 'Quotes'],
                          onTabChanged: (index) {
                            print('Tab changed to $index');
                          },
                        ),
                        const Padding(
                          padding: EdgeInsets.all(8.0),
                          child: Center(
                            child: Text('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec turpis.'),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
            AGrid(
              spacing: 16,
              areas: const [
                '4, 8',
              ],
              children: [
                const ADivider.text(text: "Pills"),
                Padding(
                  padding: const EdgeInsets.only(top: 24.0),
                  child: ACard(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        ATabHeader.text(
                          type: HeaderType.pills,
                          headerTexts: const ['Traffic', 'Purchcases', 'Quotes'],
                          onTabChanged: (index) {
                            print('Tab changed to $index');
                          },
                        ),
                        const Center(
                          child: Text('Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec turpis.'),
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildCustomTab(int index, bool selected, bool hover) {
    final textColor = !selected ? onBackgroundColor.withOpacity(0.6) : null;

    return ASpacingRow(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Text('${index + 1}', style: textTheme?.labelLarge?.copyWith(fontSize: 32, color: textColor)),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(_customTabs[index], style: textTheme?.labelLarge?.copyWith(color: textColor)),
            Text("Past Projects", style: moreDetailTextStyle.copyWith(color: textColor)),
          ],
        ),
      ],
    );
  }

  Color _getBorderColor(int index) {
    return index == 0
        ? Colors.red
        : index == 1
            ? Colors.blueAccent
            : Colors.green;
  }
}
