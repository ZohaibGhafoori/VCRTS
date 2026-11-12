public class OwnerSubmission {

    public static void send(
            String ownerID,
            String plate,
            String model,
            String type,
            double time) {

        // Run in another thread
        Thread thread = new Thread(() -> {

            OwnerRequestClient.sendRequest(
                    ownerID,
                    plate,
                    model,
                    type,
                    time
            );
        });

        thread.start();
    }
}
