import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class ElectionServer
	extends ElectionDataServiceGrpc.ElectionDataServiceImplBase
{
	private final static int PORT = 50051;

	public static void main(String[] args)
		throws InterruptedException, IOException
	{
		Server server = ServerBuilder.forPort(PORT)
			.addService(new ElectionServer())
			.build();
		System.out.println("Server starting on port 50051 ...");
		server.start();
		server.awaitTermination();
	}

	@Override
	public void sendElectionData(
		ElectionData.ElectionRequest request,
		StreamObserver<ElectionData.ElectionResponse> responseObserver)
	{
		// Access the region data from the request
		ElectionData.Region region = request.getRegion();
		System.out.println(
			"Region ID: " + region.getRegionID()
			+ "Region Name: " + region.getRegionName()
			+ "Region Address: " + region.getRegionAddress()
			+ "Region Postal Code: " + region.getRegionPostalCode()
			+ "Federal State: " + region.getFederalState()
			+ "Timestamp: " + region.getTimestamp());

		// Access the party data from the request
		for (ElectionData.Party party : request.getPartiesList())
			System.out.println(
				"Party: " + party.getPartyID() +
				", Votes: " + party.getAmountVotes());

		// Respond with a status message
		ElectionData.ElectionResponse response = ElectionData.ElectionResponse.newBuilder()
			.setStatus("Election data received successfully")
			.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}