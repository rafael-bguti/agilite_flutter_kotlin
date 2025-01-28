enum CoreProfileType { dev, prod }

late final CoreProfile activeProfile;

class CoreProfile {
  final CoreProfileType type;
  final String apiBaseUrl;

  CoreProfile({
    required this.type,
    required this.apiBaseUrl,
  });
}
