import 'dart:async';

final coreEventBus = EventBus();

class EventBus {
  final StreamController<dynamic> _streamController;

  StreamController<dynamic> get streamController => _streamController;

  EventBus({bool sync = false}) : _streamController = StreamController.broadcast(sync: sync);

  Stream<T> on<T>() {
    if (T == dynamic) {
      return streamController.stream as Stream<T>;
    } else {
      return streamController.stream.where((event) => event is T).cast<T>();
    }
  }

  void fire(event) {
    streamController.add(event);
  }

  void destroy() {
    _streamController.close();
  }
}
