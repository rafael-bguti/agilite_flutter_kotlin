import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class CardsScreen extends StatelessWidget {
  const CardsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: AContainer(
        taskHeader: "Cards",
        child: AGrid(
          spacing: 16,
          rows: const [
            '12-6, 12-6',
            '12',
            '12',
            '12',
            '12-6, 12-6',
            '12-6, 12-6',
          ],
          children: [
            ACard(
              header: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const ATitleSubtitle(title: "Raised", subtitle: "Subtitle goes here"),
                  IconButton(onPressed: () {}, icon: const Icon(Icons.more_horiz)),
                ],
              ),
              body: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text("Some quick example text to build on the card title and make up the bulk of the card's content."),
                  Padding(
                    padding: const EdgeInsets.only(top: 16.0),
                    child: FilledButton.tonal(
                      style: buildButtonStyle(primaryColor, onPrimaryColor),
                      onPressed: () {},
                      child: const Text('GET STARTED'),
                    ),
                  ),
                ],
              ),
            ),
            ACard(
              padding: EdgeInsets.zero,
              header: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const ATitleSubtitle(title: "Footer", subtitle: "Titles can go in body as well"),
                    const SizedBox(height: 16),
                    const Text("With supporting text below as a natural lead-in to additional content."),
                    const SizedBox(height: 8),
                    OutlinedButton(
                      style: buildOutlinedButtonStyle(onBackgroundColor.withOpacity(0.5)),
                      onPressed: () {},
                      child: const Text('GET STARTED'),
                    ),
                  ],
                ),
              ),
              body: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16.0),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      '2 days ago',
                      style: textTheme?.labelMedium?.copyWith(color: onBackgroundColor.withOpacity(0.5)),
                    ),
                    IconButton(onPressed: () {}, icon: const Icon(Icons.more_horiz)),
                  ],
                ),
              ),
            ),
            ACard(
              padding: EdgeInsets.zero,
              body: IntrinsicHeight(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Expanded(
                      child: Padding(
                        padding: const EdgeInsets.all(16.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const ATitleSubtitle(
                              title: "Card title",
                              subtitle: "This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.",
                            ),
                            const SizedBox(height: 16),
                            Text(
                              'Last updated 3 mins ago',
                              style: textTheme?.labelMedium?.copyWith(color: onBackgroundColor.withOpacity(0.5)),
                            ),
                          ],
                        ),
                      ),
                    ),
                    const VerticalDivider(),
                    Expanded(
                      child: Padding(
                        padding: const EdgeInsets.all(16.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const ATitleSubtitle(
                              title: "Card title",
                              subtitle:
                                  "This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.",
                            ),
                            const SizedBox(height: 16),
                            Text(
                              'Last updated 3 mins ago',
                              style: textTheme?.labelMedium?.copyWith(color: onBackgroundColor.withOpacity(0.5)),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const ASeparator(label: "Custom borders"),
            ACard(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              leftBorderColor: Colors.tealAccent,
              body: Row(
                children: [
                  const Icon(Icons.lock_clock),
                  const SizedBox(width: 16),
                  const Text("Your subscription ends on 25 February 2015"),
                  const Spacer(),
                  TextButton(onPressed: () {}, child: const Text("UPGRADE"))
                ],
              ),
            ),
            const ACard(
              leftBorderColor: Colors.blueAccent,
              body: AText(
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam aliquid eos hic magni mollitia pariatur possimus quidem quis quo sunt?",
              ),
            ),
            const ACard(
              leftBorderColor: Colors.green,
              body: AText(
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam aliquid eos hic magni mollitia pariatur possimus quidem quis quo sunt?",
              ),
            ),
            const ACard(
              leftBorderColor: Colors.orangeAccent,
              body: AText(
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam aliquid eos hic magni mollitia pariatur possimus quidem quis quo sunt?",
              ),
            ),
            const ACard(
              leftBorderColor: Colors.red,
              body: AText(
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam aliquid eos hic magni mollitia pariatur possimus quidem quis quo sunt?",
              ),
            ),
          ],
        ),
      ),
    );
  }
}
