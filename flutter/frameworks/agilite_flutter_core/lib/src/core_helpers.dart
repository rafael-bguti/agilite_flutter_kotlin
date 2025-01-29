import 'package:agilite_flutter_core/core.dart';

MetadataRepository? _metadataRepository;

set metadataRepository(MetadataRepository metadataRepository) {
  _metadataRepository = metadataRepository;
}

MetadataRepository get metadataRepository {
  return _metadataRepository ??= throw 'System initialize failed, MetadataRepository not set';
}

HttpProvider? _coreHttpProvider;

set coreHttpProvider(HttpProvider coreHttpProvider) {
  _coreHttpProvider = coreHttpProvider;
}

HttpProvider get coreHttpProvider {
  return _coreHttpProvider ??= throw 'System initialize failed, HttpProvider not set';
}

CepService? _coreCepService;

set coreCepService(CepService coreCepService) {
  _coreCepService = coreCepService;
}

CepService get coreCepService {
  return _coreCepService ??= throw 'System initialize failed, CepService not set';
}
