package de.madem.routing

import de.madem.model.api.NullableRestaurantInfo
import de.madem.modules.AppModule
import de.madem.repositories.RepositoryResponse
import de.madem.util.validation.RestaurantInfoValidator
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing

fun Routing.configureRestaurantRouting() {
    post<RestaurantRegisterRouter>{
        val registrationNullable = try{
            call.receive<NullableRestaurantInfo>()
        }
        catch (ex: Exception){
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val validator = RestaurantInfoValidator()
        if(!validator.isValid(registrationNullable)){
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val registration = registrationNullable.notNullable()
        call.respond(registration)
    }

    get<AllRestaurantsRoute>{
        when(val dbResponse = AppModule.databaseRepository.restaurants.getAll()) {
            is RepositoryResponse.Data -> call.respond(dbResponse.value)
            is RepositoryResponse.Error -> call.respond(HttpStatusCode.NotFound)
        }
    }
}