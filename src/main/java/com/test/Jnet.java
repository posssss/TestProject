package com.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacketHandler;

public class Jnet {

    public static void main(String[] args) {

        List<PcapIf> alldevs = new ArrayList<>();
        StringBuilder errbuf = new StringBuilder();
        Pcap.findAllDevs(alldevs, errbuf);
        if (alldevs.isEmpty()) {
            System.err.printf("不能获取网卡 原因： %s", errbuf
                    .toString());
            return;
        }
        int i;
        boolean isRun = true;
        while(isRun){
            i = 0;
            System.out.println("网卡驱动选择:");
            for (PcapIf device : alldevs) {
                String description = (device.getDescription() != null) ? device.getDescription() : "没有该网卡介绍信息";
                System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
            }
            Scanner input = new Scanner(System.in);
            i = input.nextInt();
            PcapIf device = alldevs.get(i);
            System.out.printf("\n选择 '%s' 网卡:\n",(device.getDescription() != null) ? device.getDescription(): device.getName());
            int snaplen = 64 * 1024;
            int flags = Pcap.MODE_PROMISCUOUS;
            int timeout = 10 * 1000;
            Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
            if (pcap == null) {
                System.err.println("打开设备捕获时出错: " + errbuf.toString());
                return;
            }
            PcapPacketHandler<String> jpacketHandler =
                    (packet, user) -> System.out.printf("接收包  %s caplen=%-4d len=%-4d %s\n",
                        new Date(packet.getCaptureHeader().timestampInMillis()),
                        packet.getCaptureHeader().caplen(), packet.getCaptureHeader().wirelen(), user);

            pcap.loop(10, jpacketHandler, "jNetPcap 运行!");
            pcap.close();
            System.out.println("是否继续？（1、 继续 / 0、 退出 ）");
            i = input.nextInt();
            if (i < 0) {
                isRun = false;
            }
        }
    }
}