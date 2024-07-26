import org.http4s.ember.server.EmberServerBuilder
import cats.effect._
import com.comcast.ip4s._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import java.time.Instant
import scodec.bits.ByteVector

object EmberServerSimpleExample extends IOApp:

  def run(args: List[String]): IO[ExitCode] =
    val server = 
      for
        server <- EmberServerBuilder
          .default[IO]
          .withHost(host"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(service[IO])
          .build
      yield server
    server.use: server =>
      IO.delay:
        println(s"Server Has Started at ${server.address}") 
      >> IO.never.as(ExitCode.Success)

  def service[F[_]: Async]: HttpApp[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._
    val now = Instant.now().getEpochSecond() / 30
    val res = for
      time <- Array(now - 1, now, now + 1)
      c = "HmacSHA1"
      // s <- Array("firstuserssecret", "seconduserssecret", "유저비밀키").map(_.getBytes).map(ByteVector(_)).map(_.toHex)
      sa = "유저비밀키".getBytes
      s = ByteVector(sa).toHex
      e = ByteVector(sa).toBase32
      gen = TOTP.gen(s, time.toHexString, 6, c)
    yield (e, gen)

    HttpRoutes
      .of[F]:
        case GET -> Root =>
          Ok(s"${res.zip(Array("Prev", "Now", "Next")).mkString(" ")}")
      .orNotFound
