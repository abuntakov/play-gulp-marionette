import play.sbt.PlayRunHook
import sbt._
import java.net.InetSocketAddress

object Webpack {
	def apply(base: File): PlayRunHook = {

		object WebpackProcess extends PlayRunHook {
			var process: Option[Process] = None

			override def beforeStarted(): Unit = {
				Process("webpack", base).run
			}

			override def afterStarted(addr: InetSocketAddress): Unit = {
				process = Some(Process("webpack --watch", base).run)
			}

			override def afterStopped(): Unit = {
				process.map(p => p.destroy())
				process = None
			}
		}

		WebpackProcess
	}
}
