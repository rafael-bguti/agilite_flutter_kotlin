import 'package:agilite_flutter_core/core.dart';

class ResponsiveSize {
  final int phone;
  final int tablet;
  final int desktop;

  const ResponsiveSize({
    this.phone = 12,
    this.tablet = 12,
    this.desktop = 12,
  })  : assert(phone >= 0 && phone <= 12),
        assert(tablet >= 0 && tablet <= 12),
        assert(desktop >= 0 && desktop <= 12);

  factory ResponsiveSize.byAreas(String query) {
    try {
      final split = query.splitToList("-");
      return ResponsiveSize(
        phone: int.parse(split[0].trim()),
        tablet: split.length >= 2 ? int.parse(split[1].trim()) : int.parse(split[0].trim()),
        desktop: int.parse(split[split.length - 1].trim()),
      );
    } catch (e) {
      throw Exception("Invalid query format. Expected format: 'phone[-tablet[-desktop]]'");
    }
  }

  int columnSize(Device device) {
    switch (device) {
      case Device.desktop:
        return desktop;
      case Device.tablet:
        return tablet;
      case Device.phone:
        return phone;
    }
  }

  double width(double maxWidth, Device device) {
    int columns = columnSize(device);
    return (columns >= 12 ? maxWidth : (columns / 12) * maxWidth).floorToDouble();
  }

  @override
  String toString() => '($phone|$tablet|$desktop)';
}
