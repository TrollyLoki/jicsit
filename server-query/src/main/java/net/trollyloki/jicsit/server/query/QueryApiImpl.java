package net.trollyloki.jicsit.server.query;

import net.trollyloki.jicsit.server.query.protocol.Message;
import net.trollyloki.jicsit.server.query.protocol.PayloadReader;
import net.trollyloki.jicsit.server.query.protocol.payload.CookiePayload;
import net.trollyloki.jicsit.server.query.protocol.payload.ServerStatePayload;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.util.Map;

@NullMarked
class QueryApiImpl implements QueryApi {

    /**
     * A request sent to the server to retrieve information about the current server state.
     */
    private static final byte POLL_SERVER_STATE = 0;

    /**
     * A response sent by the server containing the current server state.
     */
    private static final byte SERVER_STATE_RESPONSE = 1;

    private static final Map<Byte, PayloadReader<?>> PAYLOAD_READERS = Map.of(
            POLL_SERVER_STATE, CookiePayload::read,
            SERVER_STATE_RESPONSE, ServerStatePayload::read
    );

    private final QueryClient client;

    public QueryApiImpl(QueryClient client) {
        this.client = client;
    }

    @Override
    public void close() {
        client.close();
    }

    @Override
    public boolean isClosed() {
        return client.isClosed();
    }

    @Override
    public void requestServerState(long cookie) throws IOException {
        client.send(new Message(POLL_SERVER_STATE, new CookiePayload(cookie)));
    }

    @Override
    public ServerStatePayload receiveServerState() throws IOException {
        Message message;
        do {
            message = client.receive(PAYLOAD_READERS);
        } while (!(message.payload() instanceof ServerStatePayload));
        return (ServerStatePayload) message.payload();
    }

}
