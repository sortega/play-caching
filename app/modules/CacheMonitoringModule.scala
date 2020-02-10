package modules

import play.api.inject.SimpleModule
import play.api.inject._

final class CacheMonitoringModule
    extends SimpleModule((env, conf) => List(bind[CacheMonitoring].toSelf.eagerly()))
