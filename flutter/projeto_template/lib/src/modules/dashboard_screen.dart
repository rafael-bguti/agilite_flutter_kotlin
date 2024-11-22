import 'dart:math';

import 'package:agilite_flutter_core/core.dart';
import 'package:fl_chart/fl_chart.dart';
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
    return SingleChildScrollView(
      child: AContainer(
        fluid: true,
        header: AContainerHeader.text("CRM Dashboard -> ${ScreenSize(context).whichDevice()}"),
        child: ASpacingColumn(
          crossAxisAlignment: CrossAxisAlignment.start,
          spacing: 16,
          children: [
            const AAlert.warning(
              title: 'Analytics Service Issues.',
              message: 'You may experience some issues with the Analytics API. Stay up to date by following our status page.',
            ),
            AGrid(
              areas: const [
                '8, 4',
              ],
              children: const [
                _OverviewPanel(),
                _SchedulePanel(),
              ],
            ),
            const ADivider.text(text: 'Recently added'),
            const ACard(
              padding: EdgeInsets.all(8),
              child: SizedBox(
                width: double.infinity,
                child: SingleChildScrollView(
                  scrollDirection: Axis.horizontal,
                  child: Padding(
                    padding: EdgeInsets.only(bottom: 12.0),
                    child: UserTable(),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

//------
class _SchedulePanel extends StatelessWidget {
  const _SchedulePanel({super.key});

  @override
  Widget build(BuildContext context) {
    return ASpacingColumn(
      children: const [
        ADivider.text(text: 'Schedule'),
        TodayCard(),
        NextCard(),
      ],
    );
  }
}

class NextCard extends StatelessWidget {
  const NextCard({super.key});

  @override
  Widget build(BuildContext context) {
    return ACard(
      padding: EdgeInsets.zero,
      header: Padding(
        padding: const EdgeInsets.all(8),
        child: Row(
          children: [
            const AText('Upcoming', style: TextStyle(fontWeight: FontWeight.bold)),
            AText('(2)', style: TextStyle(fontWeight: FontWeight.w300, color: onBackgroundColor.withOpacity(0.3))),
            const Spacer(),
            const IconButton(
              onPressed: null,
              icon: Icon(Icons.more_horiz),
            ),
          ],
        ),
      ),
      child: Padding(
        padding: const EdgeInsets.all(8),
        child: ASpacingColumn(
          spacing: 8,
          children: [
            EventCard(
              month: "FEB",
              day: "17",
              title: "Developers Meeting",
              dayOfWeek: "Tuesday",
              startTime: "16:30",
              endTime: "18:00",
              suffix: Padding(
                padding: const EdgeInsets.only(top: 16.0),
                child: ASpacingColumn(
                  children: [
                    const AvatarsStack(),
                    AText(
                      "Moreover the striking, brilliant and vivid colors are the reason why we are attracted to the posters that we see.",
                      style: textTheme?.labelMedium?.copyWith(color: onBackgroundColor.withOpacity(0.5)),
                    ),
                  ],
                ),
              ),
            ),
            const ADivider.lineOnly(),
            const EventCard(
              month: "FEB",
              day: "18",
              title: "Meeting with Jane B.",
              dayOfWeek: "Tuesday",
              startTime: "17:30",
              endTime: "18:30",
            ),
          ],
        ),
      ),
    );
  }
}

class TodayCard extends StatelessWidget {
  const TodayCard({super.key});

  @override
  Widget build(BuildContext context) {
    return const ACard(
      padding: EdgeInsets.zero,
      header: Padding(
        padding: EdgeInsets.all(8),
        child: Row(
          children: [
            AText('Today', style: TextStyle(fontWeight: FontWeight.bold)),
            Spacer(),
            IconButton(
              onPressed: null,
              icon: Icon(Icons.more_horiz),
            ),
          ],
        ),
      ),
      child: Padding(
        padding: EdgeInsets.all(8),
        child: AAlert.warning(message: 'Nothing scheduled for today.'),
      ),
    );
  }
}

//------
class _OverviewPanel extends StatelessWidget {
  const _OverviewPanel({super.key});

  @override
  Widget build(BuildContext context) {
    return ASpacingColumn(
      children: [
        const ADivider.text(text: 'Overview'),
        AGrid(
          spacing: 8,
          areas: const [
            '6, 6',
          ],
          children: const [
            ACard(
              padding: EdgeInsets.zero,
              minHeight: 150,
              child: Padding(
                padding: EdgeInsets.symmetric(horizontal: 16, vertical: 24),
                child: _CardRow(number: '130', title: 'Contacts'),
              ),
            ),
            ACard(
              padding: EdgeInsets.zero,
              minHeight: 150,
              child: Padding(
                padding: EdgeInsets.symmetric(horizontal: 16, vertical: 24),
                child: _CardRow(number: '53', title: 'Companies'),
              ),
            ),
            ACard(
              padding: EdgeInsets.zero,
              minHeight: 150,
              child: _CardPercents(),
            ),
            ACard(
              padding: EdgeInsets.zero,
              minHeight: 150,
              child: _CardPercents2(),
            ),
          ],
        )
      ],
    );
  }
}

class _CardPercents extends StatelessWidget {
  const _CardPercents({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 24),
          child: Column(
            children: [
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  AText(
                    '20%',
                    style: textTheme?.displaySmall?.copyWith(fontFamily: 'HeaderFont'),
                  ),
                  const SizedBox(width: 8),
                  const Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      AText('Renewals', style: TextStyle(fontWeight: FontWeight.bold, fontFamily: 'HeaderFont')),
                      AText('This month'),
                    ],
                  ),
                  const Spacer(),
                  const IconButton(onPressed: null, icon: Icon(Icons.more_horiz))
                ],
              ),
            ],
          ),
        ),
        const Divider(height: 1),
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 24),
          child: ASpacingColumn(
            children: const [
              AProgressBar(
                label: "Rend A Car, Frankfurt",
                value: 12,
                barColor: Colors.orange,
                rightLabel: 'expires in 12 days',
              ),
              AProgressBar(
                label: "Rend A Car, Frankfurt",
                value: 100,
                barColor: Colors.teal,
                rightLabel: 'expires in 30 days',
              ),
            ],
          ),
        )
      ],
    );
  }
}

class _CardPercents2 extends StatelessWidget {
  const _CardPercents2({super.key});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 24),
          child: Column(
            children: [
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  AText(
                    '25%',
                    style: textTheme?.displaySmall?.copyWith(fontFamily: 'HeaderFont'),
                  ),
                  const SizedBox(width: 8),
                  const Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      AText('Conversion', style: TextStyle(fontWeight: FontWeight.bold, fontFamily: 'HeaderFont', overflow: TextOverflow.ellipsis)),
                      Wrap(
                        children: [
                          Icon(Icons.horizontal_rule, color: Colors.teal), // Ícone para o traço
                          SizedBox(width: 4), // Espaço entre o ícone e o texto
                          Text('Lead', style: TextStyle(color: Colors.black)),
                          SizedBox(width: 12), // Espaço entre os conjuntos de ícone e texto
                          Icon(Icons.horizontal_rule, color: Colors.blue),
                          SizedBox(width: 4),
                          Text('Cust.', style: TextStyle(color: Colors.black)),
                        ],
                      ),
                    ],
                  ),
                ],
              ),
            ],
          ),
        ),
        SizedBox(height: 100, child: LineChartSample())
      ],
    );
  }
}

class _CardRow extends StatelessWidget {
  final String number;
  final String title;
  const _CardRow({
    required this.number,
    required this.title,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                AText(
                  number,
                  style: textTheme?.displaySmall?.copyWith(fontFamily: 'HeaderFont'),
                ),
                AText(title, style: const TextStyle(fontWeight: FontWeight.bold, fontFamily: 'HeaderFont')),
              ],
            ),
            const IconButton(onPressed: null, icon: Icon(Icons.more_horiz))
          ],
        ),
        const SizedBox(height: 24),
        AProgressBar(label: "Customers", value: Random().nextInt(100)),
        const SizedBox(height: 10),
        AProgressBar(label: "Leads", value: Random().nextInt(100)),
        const SizedBox(height: 10),
        AProgressBar(label: "Opportunities", value: Random().nextInt(100)),
        const SizedBox(height: 10),
        AProgressBar(label: "Subscribers", value: Random().nextInt(100)),
        const SizedBox(height: 24),
        OutlinedButton(onPressed: () {}, child: Text('View $title'.toUpperCase()))
      ],
    );
  }
}

class LineChartSample extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return LineChart(
      LineChartData(
        gridData: const FlGridData(show: false),
        titlesData: const FlTitlesData(show: false),
        borderData: FlBorderData(show: false), // Remove borders
        lineBarsData: [
          LineChartBarData(
            isCurved: true,
            color: Colors.teal,
            barWidth: 3,
            isStrokeCapRound: true,
            belowBarData: BarAreaData(show: false),
            dotData: const FlDotData(show: false),
            spots: [
              const FlSpot(0, 1),
              const FlSpot(1, 1.5),
              const FlSpot(2, 1.2),
              const FlSpot(3, 2.8),
              const FlSpot(4, 2),
              const FlSpot(5, 1.5),
              const FlSpot(6, 1.1),
            ],
          ),
          LineChartBarData(
            isCurved: true,
            color: Colors.blue,
            barWidth: 3,
            isStrokeCapRound: true,
            belowBarData: BarAreaData(show: false),
            dotData: const FlDotData(show: false),
            spots: [
              const FlSpot(0, 1.2),
              const FlSpot(1, 1.8),
              const FlSpot(2, 1.4),
              const FlSpot(3, 2.5),
              const FlSpot(4, 1.9),
              const FlSpot(5, 1.6),
              const FlSpot(6, 1.2),
            ],
          ),
        ],
      ),
    );
  }
}

class EventCard extends StatelessWidget {
  final String month;
  final String day;
  final String title;
  final String dayOfWeek;
  final String startTime;
  final String endTime;
  final Widget? suffix;

  const EventCard({
    Key? key,
    required this.month,
    required this.day,
    required this.title,
    required this.dayOfWeek,
    required this.startTime,
    required this.endTime,
    this.suffix,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text(month.toUpperCase(), style: const TextStyle(fontSize: 12, color: Colors.teal)),
            Text(day, style: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold)),
            Container(
              width: 32,
              height: 2,
              color: Colors.teal,
              margin: const EdgeInsets.symmetric(vertical: 4),
            )
          ],
        ),
        const SizedBox(width: 16),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Text(title, style: textTheme?.labelMedium),
              const SizedBox(height: 4),
              Text('$dayOfWeek $startTime - $endTime', style: textTheme?.labelMedium?.copyWith(color: onBackgroundColor.withOpacity(0.5))),
              if (suffix != null) suffix!,
            ],
          ),
        ),
      ],
    );
  }
}

class AvatarsStack extends StatefulWidget {
  const AvatarsStack({super.key});

  @override
  _AvatarsStackState createState() => _AvatarsStackState();
}

class _AvatarsStackState extends State<AvatarsStack> {
  int hoveredIndex = -1;

  @override
  Widget build(BuildContext context) {
    List<Map<String, String>> users = [
      {"image": "https://i.pravatar.cc/150?img=1", "name": "Jane Doe"},
      {"image": "https://i.pravatar.cc/150?img=2", "name": "John Smith"},
      {"image": "https://i.pravatar.cc/150?img=3", "name": "Will Johnson"},
    ];

    return SizedBox(
      height: 40,
      child: Stack(
        children: List.generate(users.length, (index) {
          double offset = 25.0 * index;
          return Positioned(
            left: offset,
            child: MouseRegion(
              cursor: SystemMouseCursors.click,
              onEnter: (_) => setState(() => hoveredIndex = index),
              onExit: (_) => setState(() => hoveredIndex = -1),
              child: Transform.scale(
                scale: hoveredIndex == index ? 1.4 : 1.0,
                child: UserAvatar(
                  imageUrl: users[index]['image']!,
                  userName: users[index]['name']!,
                ),
              ),
            ),
          );
        }),
      ),
    );
  }
}

class UserAvatar extends StatelessWidget {
  final String imageUrl;
  final String userName;

  const UserAvatar({
    super.key,
    required this.imageUrl,
    required this.userName,
  });

  @override
  Widget build(BuildContext context) {
    return Tooltip(
      message: userName,
      child: CircleAvatar(
        radius: 16,
        backgroundImage: NetworkImage(imageUrl),
      ),
    );
  }
}

class UserTable extends StatefulWidget {
  const UserTable({super.key});

  @override
  _UserTableState createState() => _UserTableState();
}

class _UserTableState extends State<UserTable> {
  List<User> users = [
    User("https://i.pravatar.cc/150?img=1", 'Ollie Wallace', 'lorna.kirlin@nora.biz', null, 'Manager', '285-626-6050', '16 February 2019', false),
    User("https://i.pravatar.cc/150?img=5", 'Gilbert Barrett', 'paolo.zieme@gmail.com', null, 'Admin', '462-060-7408', '17 February 2019', false),
    User("https://i.pravatar.cc/150?img=8", 'Tony Parks', 'vida.glover@gmail.com', Company('Frontend Matter Inc', 'Leuschkefurt'), 'Admin', '169-769-4821', '18 February 2019',
        true),
    User(
        "https://i.pravatar.cc/150?img=9", 'Billy Nunez', 'annabell.kris@yahoo.com', Company('Huma Huma Inc.', 'Huma Huma Inc.'), 'User', '239-721-3649', '19 February 2019', true),
  ];

  @override
  Widget build(BuildContext context) {
    return DataTable(
      columns: const <DataColumn>[
        DataColumn(label: Text('Name')),
        DataColumn(label: Text('Company')),
        DataColumn(label: Text('Tags')),
        DataColumn(label: Text('Phone')),
        DataColumn(label: Text('Added')),
      ],
      rows: users
          .map((user) => DataRow(
                  selected: user.selected,
                  onSelectChanged: (bool? value) {
                    setState(() {
                      user.selected = value!;
                    });
                  },
                  cells: [
                    DataCell(
                      ASpacingRow(
                        children: [
                          CircleAvatar(
                            radius: 16,
                            backgroundImage: NetworkImage(user.image),
                          ),
                          Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                user.name,
                                style: textTheme?.labelLarge,
                              ),
                              Text(
                                user.email,
                                style: textTheme?.labelMedium?.copyWith(fontWeight: FontWeight.normal),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                    DataCell(
                      user.company == null
                          ? const Text('')
                          : Row(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                CompanyAvatar(userName: user.company!.name),
                                Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Text(user.company!.name, style: textTheme?.labelLarge),
                                    Text(user.company!.location, style: textTheme?.labelMedium?.copyWith(fontWeight: FontWeight.normal)),
                                  ],
                                ),
                              ],
                            ),
                    ),
                    DataCell(Chip(label: Text(user.tags))),
                    DataCell(Text(user.phone)),
                    DataCell(Text(user.added)),
                  ]))
          .toList(),
    );
  }
}

class CompanyAvatar extends StatelessWidget {
  final String userName;
  final double size;

  const CompanyAvatar({super.key, required this.userName, this.size = 40.0});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: size,
      height: size,
      decoration: BoxDecoration(
        color: _randomColor(),
        borderRadius: BorderRadius.circular(4.0), // Ajuste para mais ou menos arredondado
      ),
      margin: const EdgeInsets.only(right: 8),
      child: Center(
        child: Text(
          _getInitials(userName),
          style: const TextStyle(
            fontSize: 13,
            fontWeight: FontWeight.bold,
            color: Colors.white,
          ),
        ),
      ),
    );
  }

  // Função para extrair as duas primeiras letras do nome
  String _getInitials(String name) {
    List<String> names = name.split(' ');
    String initials = names[0][0];
    if (names.length > 1) {
      initials += names[1][0];
    }
    return initials.toUpperCase();
  }

  // Função para gerar uma cor aleatória
  Color _randomColor() {
    Random random = Random();
    return Color.fromRGBO(
      random.nextInt(256),
      random.nextInt(256),
      random.nextInt(256),
      1,
    );
  }
}

class User {
  final String image;
  final String name;
  final String email;
  final Company? company;
  final String tags;
  final String phone;
  final String added;
  bool selected;

  User(this.image, this.name, this.email, this.company, this.tags, this.phone, this.added, this.selected);
}

class Company {
  final String name;
  final String location;

  Company(this.name, this.location);
}
