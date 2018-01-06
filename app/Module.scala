import com.google.inject.AbstractModule
import database.provider.api.{SlickDatabaseProvider, SlickSessionProvider}
import database.provider.{ApplicationDatabaseProvider, SlickH2SessionProvider}
import lifecycle.Initializable
import lifecycle.sample.SampleInitializable

/**
  * This class is a Guice module that tells Guice how to bind several
  * different types. This Guice module is created when the Play
  * application starts.
  *
  * Play will automatically use any class called `Module` that is in
  * the root package. You can create modules in other locations by
  * adding `play.modules.enabled` settings to the `application.conf`
  * configuration file.
  */
class Module extends AbstractModule {
  override def configure(): Unit = {
    /** Execute before application started */
    bind(classOf[Initializable])
      .to(classOf[SampleInitializable])
      .asEagerSingleton()

    bind(classOf[SlickSessionProvider])
    .to(classOf[SlickH2SessionProvider])

    bind(classOf[SlickDatabaseProvider])
      .to(classOf[ApplicationDatabaseProvider])
  }
}
