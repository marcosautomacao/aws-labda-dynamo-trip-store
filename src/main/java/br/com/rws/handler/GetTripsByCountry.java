package br.com.rws.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.rws.models.HandlerRequest;
import br.com.rws.models.HandlerResponse;

public class GetTripsByCountry implements RequestHandler<HandlerRequest, HandlerResponse>{

	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}