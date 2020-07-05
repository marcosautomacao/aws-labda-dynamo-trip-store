package br.com.rws.handler;

import br.com.rws.DAO.TripRepository;
import br.com.rws.models.Trip;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.rws.models.HandlerRequest;
import br.com.rws.models.HandlerResponse;

import java.util.List;

public class GetTripsByCity implements RequestHandler<HandlerRequest, HandlerResponse>{


	private final TripRepository repository = new TripRepository();

	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {

		final String city = request.getPathParameters().get("city");

		context.getLogger().log("Buscando por viagens para a cidade = " + city);

		try {

		final List<Trip> trips = this.repository.findByCity(city);

		if (trips == null || trips.isEmpty()) {
			return HandlerResponse.builder().setStatusCode(404).build();
		}
		return HandlerResponse.builder().setStatusCode(200).setObjectBody(trips).build();
	} catch (Exception e) {
		return HandlerResponse.builder().setStatusCode(400).setRawBody("There is a error in persistence >>" + e.getMessage()).build();
	}
	}

}
