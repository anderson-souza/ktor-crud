package aps.com.routes

import aps.com.model.Customer
import aps.com.model.customerStorage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.customerRouting() {
    route("/customers") {

        getAllCustomers()

        getCustomerById()

        createNewCustomer()

        deleteCustomerById()
    }
}

private fun Route.getAllCustomers() {
    get {
        if (customerStorage.isNotEmpty()) {
            call.respond(customerStorage)
        } else {
            call.respondText("Consumidor não encontrado", status = HttpStatusCode.NotFound)
        }
    }
}

private fun Route.getCustomerById() {
    get("{id}") {

        val id = call.parameters["id"] ?: return@get call.respondText(
            "Está faltando ID", status = HttpStatusCode.BadRequest
        )

        val customer =
            customerStorage.find { it.id == id } ?: return@get call.respondText(
                "Não foi encontrado Consumidor com o ID $id",
                status = HttpStatusCode.NotFound
            )
        call.respond(customer)
    }
}

private fun Route.createNewCustomer() {
    post {
        val customer = call.receive<Customer>()
        customerStorage.add(customer)
        call.respondText("Consumidor Criado", status = HttpStatusCode.Created)
    }
}

private fun Route.deleteCustomerById() {
    delete("{id}") {
        val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
        if (customerStorage.removeIf { it.id == id }) {
            call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
        } else {
            call.respondText("Not Found", status = HttpStatusCode.NotFound)
        }
    }
}

fun Application.registerCustomerRoutes() {
    routing {
        customerRouting()
    }
}