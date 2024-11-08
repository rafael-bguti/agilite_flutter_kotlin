import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class DashboardScreen extends StatefulWidget {
  const DashboardScreen({super.key});

  @override
  State<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends State<DashboardScreen> {
  @override
  void initState() {
    super.initState();

    runOnNextBuild(() {
      fullLayoutFacade.selectItemByRoute(dashboardPath);
    });
  }

  @override
  Widget build(BuildContext context) {
    return AFluidTaskLayout(
      taskHeader: "CRM Dashboard",
      child: ASpacingColumn(
        spacing: 16,
        children: [
          const AAlert.warning(
            title: 'Analytics Service Issues.',
            message: 'You may experience some issues with the Analytics API. Stay up to date by following our status page.',
          ),
          _OverviewPanel(),
          // AGrid(
          //   linhas: const [
          //     '12-8, 12-4',
          //   ],
          //   children: const [
          //     _OverviewPanel(),
          //     _SchedulePanel(),
          //   ],
          // ),
        ],
      ),
    );
  }
}

class _OverviewPanel extends StatelessWidget {
  const _OverviewPanel({super.key});

  @override
  Widget build(BuildContext context) {
    return ASpacingColumn(
      children: [
        const ASeparator(label: 'Overview'),
        AGrid(
          spacing: 8,
          linhas: const [
            '12-6, 12-6',
          ],
          children: const [
            ACard(
              minHeight: 150,
              child: Text("a"),
            ),
            ACard(
              minHeight: 150,
              child: Text("b"),
            ),
            ACard(
              minHeight: 150,
              child: Text("c"),
            ),
            ACard(
              minHeight: 150,
              child: Text("d"),
            ),
          ],
        )
      ],
    );
  }
}

class _SchedulePanel extends StatelessWidget {
  const _SchedulePanel({super.key});

  @override
  Widget build(BuildContext context) {
    return ASpacingColumn(
      children: const [
        ASeparator(label: 'Schedule'),
      ],
    );
  }
}
