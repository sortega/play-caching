package controllers

import javax.inject._
import scala.concurrent.duration._

import com.kenshoo.play.metrics.Metrics
import play.api._
import play.api.cache._
import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class FibonacciController @Inject()(
  val cache: SyncCacheApi,
  val metrics: Metrics,
  val controllerComponents: ControllerComponents
) extends BaseController {
  private val count = metrics.defaultRegistry.counter("fib.count")

  def index(n: Int) = Action { implicit request =>
    count.inc()
    val cacheKey = n.toString
    val result = cache.getOrElseUpdate[Int](cacheKey, expiration = 1.minute)(slowFib(n))
    Ok(Json.toJson(result))
  }

  private def slowFib(n: Int): Int = {
    if (n <= 1) 1
    else {
      Thread.sleep(100)
      slowFib(n - 1) + slowFib(n - 2)
    }
  }
}
