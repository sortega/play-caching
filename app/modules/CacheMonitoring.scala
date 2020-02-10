package modules

import java.util
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.jdk.CollectionConverters._

import com.codahale.metrics.Gauge
import com.kenshoo.play.metrics.Metrics
import net.sf.ehcache.{CacheManager, Ehcache}
import play.api.inject.ApplicationLifecycle

@Singleton
final class CacheMonitoring @Inject()(
  val cacheManager: CacheManager,
  val metrics: Metrics,
  val applicationLifecycle: ApplicationLifecycle
) {
  val gauge = metrics.defaultRegistry.gauge("ehcache.stats", () => new Gauge[util.Map[String, Long]] {
    override def getValue: util.Map[String, Long] = {
      Map(
        "size" -> defaultCache.getStatistics.getSize,
        "hits" -> defaultCache.getStatistics.cacheHitCount(),
        "misses" -> defaultCache.getStatistics.cacheMissCount(),
        "evictions" -> defaultCache.getStatistics.cacheEvictedCount()
      ).asJava
    }
  })

  private def defaultCache: Ehcache = cacheManager.getEhcache("play")

  applicationLifecycle.addStopHook(() => {
    metrics.defaultRegistry.remove("ehcache.stats")
    Future.successful(println("Bye"))
  })
}
