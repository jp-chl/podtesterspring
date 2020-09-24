package nectia.com.podtesterspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class PodtesterspringApplication {

	final String[][] ENDPOINTS = new String[][] {
			{"/", "Hello world from <hostname>"},
			{"/healthz/ready", "Liveness probe"},
			{"/misbehave", "Emulates liveness to return HTTP Code 503"},
			{"/behave", "Emulates liveness to return HTTP Code 200"},
			{"/callanother?url=", "Call an endpoint from this component (i.e. container)"},
			{"/sysresources", "Used memory, Max memory and Cores"},
			{"/consume?percentage", "Endpoint to consume memory"}
	};

	final String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
	String greeting;
	private int count = 0;
	boolean behave = true;
	RestTemplate restTemplate = new RestTemplate();

	public PodtesterspringApplication() {
	} // end PodtesterspringApplication()

	public static void main(String[] args) {
		SpringApplication.run(PodtesterspringApplication.class, args);
	} // end void main(String[] args)

	/**
	 * Main endpoint (/)
	 * @return Hello World string + hostname + this method's invocation count
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String home() {
		count++;
		final String returnMessage = "Hello World from hostname: ("+hostname+"). Called " + count + " times";
		System.out.println(returnMessage);
		return returnMessage;
	} // end String home()

	/**
	 * Liveness endpoint (/healthz/ready)
	 * @return HttpCode + message
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/healthz/ready")
	public ResponseEntity<String> health() {
		if ( behave ) {
			return ResponseEntity.status(HttpStatus.OK)
					.body("I am alive");
		}

		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("I am down");
	} // end ResponseEntity<String> health()

	/**
	 * Endpoint to emulate misbehaving
	 * @return HttpCode + message
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/misbehave")
	public ResponseEntity<String> misbehave() {
		behave = false;
		return ResponseEntity.status(HttpStatus.OK).body("Misbehaving...");
	} // end ResponseEntity<String> misbehave()

	/**
	 * Endpoint to emulate behaving
	 * @return HttpCode + message
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/behave")
	public ResponseEntity<String> behave() {
		behave = true;
		return ResponseEntity.status(HttpStatus.OK).body("Not misbehaving...");
	} // end ResponseEntity<String> behave()

	/**
	 * Call an endpoint from this component (i.e. container)
	 *
	 * Utility to call another endpoint in a K8s cluster by
	 * calling url based on its inner dns (example: http://backend.default.svc.cluster.local:3000)
	 *
	 * @param url URL to call from
	 * @return endpoint's response as String
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/callanother")
	public String callanother(@RequestParam("url") String url) {

		// <servicename>.<namespace>.svc.cluster.local
		//String url = "http://mynode.yourspace.svc.cluster.local:8000/";

		ResponseEntity<String> response = restTemplate.getForEntity("http://" + url, String.class);

		String responseBody =  response.getBody();
		System.out.println("/callanother?url="+url+". Returns: " + responseBody);

		return responseBody;
	} // String callanother(@RequestParam("url") String url)

	/**
	 * Endpoint to check system resources
	 * @return Used memory, Max memory and Cores
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/sysresources")
	public String getSystemResources() {
		final Runtime rt = Runtime.getRuntime();
		final long maxMemory = rt.maxMemory() / 1024 / 1024;
		final long usedMemory = rt.totalMemory() / 1024 / 1024;
		final long cores  = rt.availableProcessors();

		final String response = "Used memory: " + usedMemory + ". Max memory: " + maxMemory + ", Cores: " + cores;

		System.out.println("/sysresources. " + response);

		return response + "\n";
	} // end String getSystemResources()

	/**
	 * Endpoint to consume memory
	 * @param percentage max percentage of memory to consume
	 * @return Informative message about consumption process result
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/consume")
	public String consumeMemory(@RequestParam(value = "percentage", defaultValue = "80") int percentage) {
		System.out.println("/consume?percentage=" + percentage);
		final long lPercentage = percentage/100;

		final Runtime rt = Runtime.getRuntime();
		StringBuilder sb = new StringBuilder();
		long maxMemory = rt.maxMemory();
		long usedMemory = 0;

		while(((float) usedMemory / maxMemory) < lPercentage) {
			sb.append(System.nanoTime()).append(sb.toString());
			usedMemory = rt.totalMemory();
		} // end memory consumption loop

		String msg = "Allocated about "
				+ percentage + "% ("
				+ humanReadableByteCount(usedMemory, false)
				+ ") of max allowed JVM memory size ("
				+ humanReadableByteCount(maxMemory, false)
				+ ")";
		System.out.println(msg);

		return msg + "\n";
	} // end String consumeMemory(@RequestParam("percentage") int percentage)

	/**
	 * Describe this app's endpoints
	 * @return list of endpoints' name and description
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/endpoints")
	public String getEndpoints() {
		StringBuffer sb = new StringBuffer();

		for(String[] endpoint : ENDPOINTS) {
			sb.append(endpoint[0])
					.append(": ")
					.append(endpoint[1])
					.append("\n");
		} //

		System.out.println("/endpoints called");

		return sb.toString();
	} // end String getEndpoints()

	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	} // end  String humanReadableByteCount(long bytes, boolean si)

} // end PodtesterspringApplication
