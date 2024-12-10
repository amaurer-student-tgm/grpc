import grpc
import election_data_pb2
import election_data_pb2_grpc

def run_client():
    server_address = "localhost:50051"
    channel = grpc.insecure_channel(server_address)
    stub = election_data_pb2_grpc.ElectionDataServiceStub(channel)

    # set the region
    region = election_data_pb2.Region(
        regionID=33123,
        regionName="Linz Bahnhof",
        regionAddress="Bahnhofsstrasse 27/9",
        regionPostalCode="Linz",
        federalState="Austria",
        timestamp="2024-09-12 11:48:21"
    )

    # set the parties
    parties = [
        election_data_pb2.Party(partyID="OEVP", amountVotes=322),
        election_data_pb2.Party(partyID="SPOE", amountVotes=301),
        election_data_pb2.Party(partyID="FPOE", amountVotes=231),
        election_data_pb2.Party(partyID="GRUENE", amountVotes=211),
        election_data_pb2.Party(partyID="NEOS", amountVotes=182)
    ]

    request = election_data_pb2.ElectionRequest(region=region, parties=parties)

    # error handeling
    try:
        response = stub.sendElectionData(request)
        print(f"Server response: {response.status}")
    except grpc.RpcError as e:
        print(f"gRPC error: {e.code()} - {e.details()}")

    channel.close()

if __name__ == "__main__":
    run_client()