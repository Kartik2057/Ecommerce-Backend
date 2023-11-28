package com.kartik.Ecommerce.controllers;


import com.kartik.Ecommerce.exception.OrderException;
import com.kartik.Ecommerce.model.Order;
import com.kartik.Ecommerce.model.OrderStatus;
import com.kartik.Ecommerce.repositories.OrderRepository;
import com.kartik.Ecommerce.response.ApiResponse;
import com.kartik.Ecommerce.response.PaymentLinkResponse;
import com.kartik.Ecommerce.services.OrderService;
import com.kartik.Ecommerce.services.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Value("${razorpay.api.key}")
    String apiKey;
    @Value("${razorpay.api.secret}")
    String apiSecret;

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws RazorpayException, OrderException {
        Order order = orderService.findOrderById(orderId);

        try{
            RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecret);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",order.getTotalDiscountedPrice()*100);
            paymentLinkRequest.put("currency","INR");

            JSONObject customer = new JSONObject();
            customer.put("name",order.getShippingAddress().getFirstName());
            customer.put("email",order.getUser().getEmail());
            customer.put("contact",order.getShippingAddress().getMobile());
            paymentLinkRequest.put("customer",customer);
            System.out.println("Customer "+customer);

            JSONObject notify = new JSONObject();
            notify.put("sms",true);
            notify.put("email",true);

            paymentLinkRequest.put("notify",notify);
            paymentLinkRequest.put("callback_url","http://localhost:3000/payment/"+orderId);
            paymentLinkRequest.put("callback_method","get");


            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
            System.out.println("paymentLink "+paymentLink);

            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl = paymentLink.get("short_url");

            PaymentLinkResponse res = new PaymentLinkResponse();
            res.setPayment_link_id(paymentLinkId);
            res.setPayment_link_url(paymentLinkUrl);
            return new ResponseEntity<PaymentLinkResponse>(res, HttpStatus.CREATED);
        }catch(Exception e){
            throw new RazorpayException(e.getMessage());
        }
    }

    public ResponseEntity<ApiResponse> redirect(
            @RequestParam(name="payment_id")String paymentId,
            @RequestParam(name = "order_id")Long orderId
            ) throws OrderException, RazorpayException {
            Order order = orderService.findOrderById(orderId);
            RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecret);
            try {
                Payment payment = razorpayClient.payments.fetch(paymentId);

                if (payment.get("status").equals("captured")){
                    order.getPaymentDetails().setPaymentId(paymentId);
                    order.getPaymentDetails().setStatus("COMPLETED");
                    order.setOrderStatus(OrderStatus.ORDER_PLACED);
                    orderRepository.save(order);
                }

                ApiResponse response = new ApiResponse("Your Order got placed",true);
                return new ResponseEntity<ApiResponse>(response,HttpStatus.ACCEPTED);
            }catch (Exception e){
                throw new RazorpayException(e.getMessage());
            }
    }
}
