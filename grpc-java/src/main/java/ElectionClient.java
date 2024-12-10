import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.ArrayList;
import java.util.List;

public class ElectionClient {
	private final static int PORT = 50051;

	public static void main(String[] args) {
		ManagedChannel channel =
			ManagedChannelBuilder.forAddress("localhost", PORT)
			.usePlaintext()
			.build();

		ElectionDataServiceGrpc.ElectionDataServiceBlockingStub stub =
			ElectionDataServiceGrpc.newBlockingStub(channel);

		// Create the region
		ElectionData.Region region = ElectionData.Region.newBuilder()
			.setRegionID(33123)
			.setRegionName("Linz Bahnhof")
			.setRegionAddress("Bahnhofsstrasse 27/9")
			.setRegionPostalCode("Linz")
			.setFederalState("Austria")
			.setTimestamp("2024-09-12 11:48:21")
			.build();

		// Create the parties
		List<ElectionData.Party> parties = new ArrayList<>();
		parties.add(ElectionData.Party.newBuilder().setPartyID("OEVP").setAmountVotes(322).build());
		parties.add(ElectionData.Party.newBuilder().setPartyID("SPOE").setAmountVotes(301).build());
		parties.add(ElectionData.Party.newBuilder().setPartyID("FPOE").setAmountVotes(231).build());
		parties.add(ElectionData.Party.newBuilder().setPartyID("GRUENE").setAmountVotes(211).build());
		parties.add(ElectionData.Party.newBuilder().setPartyID("NEOS").setAmountVotes(182).build());

		// Create the request
		ElectionData.ElectionRequest request = ElectionData.ElectionRequest.newBuilder()
			.setRegion(region)
			.addAllParties(parties)
			.build();

		// Send the data and receive the status
		ElectionData.ElectionResponse response = stub.sendElectionData(request);
		System.out.println("Server response: " + response.getStatus());

		channel.shutdown();
	}
}