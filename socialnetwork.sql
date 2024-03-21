--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0
-- Dumped by pg_dump version 16.0

-- Started on 2024-03-21 15:06:53

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 16430)
-- Name: friendships; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.friendships (
    idu1 bigint NOT NULL,
    idu2 bigint NOT NULL,
    friends_from timestamp without time zone
);


ALTER TABLE public.friendships OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16455)
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
    idm integer NOT NULL,
    from_user bigint,
    to_user bigint,
    mesaj character varying,
    data_trimiterii timestamp without time zone,
    reply_id bigint
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16454)
-- Name: messages_idm_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.messages_idm_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.messages_idm_seq OWNER TO postgres;

--
-- TOC entry 4819 (class 0 OID 0)
-- Dependencies: 220
-- Name: messages_idm_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.messages_idm_seq OWNED BY public.messages.idm;


--
-- TOC entry 219 (class 1259 OID 16446)
-- Name: requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.requests (
    idr integer NOT NULL,
    id1 bigint,
    id2 bigint,
    status character varying
);


ALTER TABLE public.requests OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16445)
-- Name: requests_idr_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.requests_idr_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.requests_idr_seq OWNER TO postgres;

--
-- TOC entry 4820 (class 0 OID 0)
-- Dependencies: 218
-- Name: requests_idr_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.requests_idr_seq OWNED BY public.requests.idr;


--
-- TOC entry 215 (class 1259 OID 16411)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    first_name character varying,
    last_name character varying,
    username character varying,
    password character varying
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16427)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 4649 (class 2604 OID 16458)
-- Name: messages idm; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages ALTER COLUMN idm SET DEFAULT nextval('public.messages_idm_seq'::regclass);


--
-- TOC entry 4648 (class 2604 OID 16449)
-- Name: requests idr; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.requests ALTER COLUMN idr SET DEFAULT nextval('public.requests_idr_seq'::regclass);


--
-- TOC entry 4809 (class 0 OID 16430)
-- Dependencies: 217
-- Data for Name: friendships; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.friendships (idu1, idu2, friends_from) FROM stdin;
10	11	2023-11-28 16:43:31
25	27	2023-11-28 16:43:52
2	25	2023-12-09 17:52:23
2	27	2023-12-12 16:17:57
10	25	2024-01-18 16:28:59
\.


--
-- TOC entry 4813 (class 0 OID 16455)
-- Dependencies: 221
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.messages (idm, from_user, to_user, mesaj, data_trimiterii, reply_id) FROM stdin;
1	10	11	Salut	2023-12-11 22:13:01	\N
2	10	25	Salut	2023-12-11 22:41:16	\N
3	10	11	Ce faci?	2023-12-11 22:41:30	\N
4	10	25	Ce faci?	2023-12-11 22:41:30	\N
5	11	10	Bine, tu?	2023-12-11 23:07:53	\N
6	10	11	Si eu bine	2023-12-11 23:09:00	\N
7	10	11	Acum mi-am terminat temele	2023-12-12 14:24:54	6
8	11	10	Nice	2023-12-12 14:25:52	7
9	25	2	Salut!	2023-12-12 16:23:05	\N
10	25	25	Salut!	2023-12-12 16:23:05	\N
11	25	27	Salut!	2023-12-12 16:23:05	\N
12	27	25	Salutare!	2023-12-12 16:23:48	11
13	27	25	Ce mai faci?	2023-12-12 16:24:10	\N
\.


--
-- TOC entry 4811 (class 0 OID 16446)
-- Dependencies: 219
-- Data for Name: requests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.requests (idr, id1, id2, status) FROM stdin;
17	11	31	PENDING
\.


--
-- TOC entry 4807 (class 0 OID 16411)
-- Dependencies: 215
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, first_name, last_name, username, password) FROM stdin;
2	Vlad	Stan	vladstan	4rVj/RjDgNjF4jwH3cyX4w==
25	Emanuel	Pasca	emanuelpasca	4rVj/RjDgNjF4jwH3cyX4w==
10	Andrei	Popescu	andreipopescu	4rVj/RjDgNjF4jwH3cyX4w==
11	Mihai	Supuran	mihaisupuran	4rVj/RjDgNjF4jwH3cyX4w==
27	John	Smith	johnsmith	4rVj/RjDgNjF4jwH3cyX4w==
29	Ion	Pop	ionpop	4rVj/RjDgNjF4jwH3cyX4w==
30	Laur	Popa	laurpopa	4rVj/RjDgNjF4jwH3cyX4w==
31	Andrei	Pop	apop	4rVj/RjDgNjF4jwH3cyX4w==
\.


--
-- TOC entry 4821 (class 0 OID 0)
-- Dependencies: 220
-- Name: messages_idm_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.messages_idm_seq', 13, true);


--
-- TOC entry 4822 (class 0 OID 0)
-- Dependencies: 218
-- Name: requests_idr_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.requests_idr_seq', 17, true);


--
-- TOC entry 4823 (class 0 OID 0)
-- Dependencies: 216
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 31, true);


--
-- TOC entry 4659 (class 2606 OID 16462)
-- Name: messages messages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (idm);


--
-- TOC entry 4653 (class 2606 OID 16434)
-- Name: friendships pk_friendship; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT pk_friendship PRIMARY KEY (idu1, idu2);


--
-- TOC entry 4651 (class 2606 OID 16429)
-- Name: users pk_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT pk_id PRIMARY KEY (id);


--
-- TOC entry 4655 (class 2606 OID 16453)
-- Name: requests requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.requests
    ADD CONSTRAINT requests_pkey PRIMARY KEY (idr);


--
-- TOC entry 4656 (class 1259 OID 16468)
-- Name: fki_fk_from; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_fk_from ON public.messages USING btree (from_user);


--
-- TOC entry 4657 (class 1259 OID 16474)
-- Name: fki_fk_to; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_fk_to ON public.messages USING btree (to_user);


--
-- TOC entry 4662 (class 2606 OID 16463)
-- Name: messages fk_from; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk_from FOREIGN KEY (from_user) REFERENCES public.users(id) ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4660 (class 2606 OID 16435)
-- Name: friendships fk_idu1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT fk_idu1 FOREIGN KEY (idu1) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4661 (class 2606 OID 16440)
-- Name: friendships fk_idu2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friendships
    ADD CONSTRAINT fk_idu2 FOREIGN KEY (idu2) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4663 (class 2606 OID 16469)
-- Name: messages fk_to; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT fk_to FOREIGN KEY (to_user) REFERENCES public.users(id) ON DELETE CASCADE NOT VALID;


-- Completed on 2024-03-21 15:06:54

--
-- PostgreSQL database dump complete
--

