import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

import '../models/error_state.dart';
import 'a_error.dart';

class AFutureBuilder extends StatelessWidget {
  final Future<Widget> future;
  final Widget? loading;

  const AFutureBuilder({
    super.key,
    required this.future,
    this.loading,
  });

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: future,
      builder: (context, snapshot) {
        if (snapshot.connectionState != ConnectionState.done) {
          return loading ?? coreStyle.loadingWidget;
        }
        if (!snapshot.hasData) {
          return AErrorView(
            state: ErrorState(
              error: 'NoDataFoundOnFutureLoading',
              stack: StackTrace.current,
              title: 'No data found on future loading',
              onBackButtonPressed: () => ANavigator.go(dashboardPath),
            ),
          );
        }

        return snapshot.data!;
      },
    );
  }
}
